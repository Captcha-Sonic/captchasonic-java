package com.captchasonic;

import com.google.protobuf.ByteString;
import com.captchasonic.v1.CreateTaskRequest;
import com.captchasonic.v1.CreateTaskResponse;
import com.captchasonic.v1.GetBalanceRequest;
import com.captchasonic.v1.GetTaskResultRequest;
import com.captchasonic.v1.GetTaskResultResponse;
import com.captchasonic.v1.HealthCheckRequest;
import com.captchasonic.v1.HealthCheckResponse;
import com.captchasonic.v1.SonicServiceGrpc;
import com.captchasonic.v1.Task;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Official Java client for the CaptchaSonic gRPC CAPTCHA solving API.
 *
 * <p>Supports both blocking calls and async {@link CompletableFuture} variants.
 * Implements {@link AutoCloseable} for use in try-with-resources.
 *
 * <h3>Simple constructor:</h3>
 * <pre>{@code
 * try (var client = new CaptchaSonicClient("sonic_xxx")) {
 *     var result = client.solvePopularCaptcha(List.of(img), "Select all cats", "objectClassify");
 * }
 * }</pre>
 *
 * <h3>Builder pattern:</h3>
 * <pre>{@code
 * CaptchaSonicClient client = CaptchaSonicClient.builder()
 *     .apiKey("sonic_xxx")
 *     .host("api.captchasonic.com")
 *     .timeout(Duration.ofSeconds(120))
 *     .build();
 * }</pre>
 */
public class CaptchaSonicClient implements AutoCloseable {

    /** Default production gRPC host. */
    public static final String DEFAULT_HOST = "api.captchasonic.com";
    /** Default production gRPC port. */
    public static final int    DEFAULT_PORT = 443;

    private static final int    RETRY_ATTEMPTS   = 3;
    private static final long   POLL_INTERVAL_MS = 2_000L;
    private static final long   POLL_MAX_WAIT_MS = 120_000L;
    private static final int    MAX_MSG_BYTES    = 50 * 1024 * 1024; // 50 MB

    private final String apiKey;
    private final ManagedChannel channel;
    private final SonicServiceGrpc.SonicServiceBlockingStub blockingStub;

    /** Returns a new {@link CaptchaSonicOptions.Builder} for fluent configuration. */
    public static CaptchaSonicOptions.Builder builder() {
        return CaptchaSonicOptions.builder();
    }

    /**
     * Creates a production client connecting to {@value #DEFAULT_HOST}:{@value #DEFAULT_PORT} with TLS.
     * @param apiKey your CaptchaSonic API key (must start with {@code sonic_})
     */
    public CaptchaSonicClient(String apiKey) {
        this(apiKey, DEFAULT_HOST, DEFAULT_PORT, true);
    }

    /**
     * Creates a client with TLS enabled on a custom host and port.
     * @param apiKey  your CaptchaSonic API key
     * @param host    gRPC host, e.g. {@code "api.captchasonic.com"}
     * @param port    gRPC port, e.g. {@code 443}
     */
    public CaptchaSonicClient(String apiKey, String host, int port) {
        this(apiKey, host, port, true);
    }

    /**
     * Creates a client with configurable TLS.
     * @param apiKey  your CaptchaSonic API key
     * @param host    gRPC host
     * @param port    gRPC port
     * @param useTls  {@code false} for plaintext (local dev)
     */
    public CaptchaSonicClient(String apiKey, String host, int port, boolean useTls) {
        validateApiKey(apiKey);
        this.apiKey = apiKey;
        ManagedChannelBuilder<?> builder = ManagedChannelBuilder.forAddress(host, port)
                .keepAliveTime(10, TimeUnit.SECONDS)
                .keepAliveTimeout(5, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .maxInboundMessageSize(MAX_MSG_BYTES);
        if (!useTls) {
            builder.usePlaintext();
        }
        this.channel = builder.build();
        this.blockingStub = SonicServiceGrpc.newBlockingStub(channel);
    }

    /** Creates a client from {@link CaptchaSonicOptions} (used by the builder). */
    CaptchaSonicClient(CaptchaSonicOptions options) {
        this(options.getApiKey(), options.getHost(), options.getPort(), options.isUseTls());
    }


    /**
     * Submits a CAPTCHA task and returns the solution.
     * Automatically polls for async token tasks (Turnstile etc.).
     */
    public CreateTaskResponse createTask(Task task) {
        CreateTaskRequest request = CreateTaskRequest.newBuilder()
                .setApiKey(apiKey)
                .setTask(task)
                .build();
        CreateTaskResponse response = withRetry(() -> blockingStub.createTask(request));

        if (response.getErrorId() != 0) {
            throwForErrorId(response.getErrorId(), response.getErrorDescription());
        }

        String status = response.getStatus();
        if (!response.getTaskId().isEmpty() && ("processing".equals(status) || "pending".equals(status))) {
            return pollTask(response.getTaskId());
        }
        return response;
    }

    /** Manually fetches the result of an async (token) task. */
    public GetTaskResultResponse getTaskResult(String taskId) {
        GetTaskResultRequest request = GetTaskResultRequest.newBuilder()
                .setApiKey(apiKey)
                .setTaskId(taskId)
                .build();
        return withRetry(() -> blockingStub.getTaskResult(request));
    }

    /** Returns the current account balance in USD. */
    public double getBalance() {
        GetBalanceRequest request = GetBalanceRequest.newBuilder().setApiKey(apiKey).build();
        var response = withRetry(() -> blockingStub.getBalance(request));
        if (response.getErrorId() != 0) {
            throwForErrorId(response.getErrorId(), "");
        }
        return response.getBalance();
    }

    /** Checks server availability. No API key required. */
    public HealthCheckResponse healthCheck() {
        return withRetry(() -> blockingStub.healthCheck(HealthCheckRequest.getDefaultInstance()));
    }


    /**
     * Solve a PopularCaptcha image challenge.
     *
     * @param images       Challenge tile images as raw bytes.
     * @param question     The challenge question (natural language).
     * @param questionType PopularCaptcha question type, e.g. {@code "objectClassify"}. Mandatory.
     */
    public CreateTaskResponse solvePopularCaptcha(List<byte[]> images, String question,
            String questionType) {
        return solvePopularCaptcha(images, question, questionType, null, false, null, null);
    }

    /**
     * Solve a PopularCaptcha challenge with optional example images, screenshot flag,
     * and website context (URL + site key).
     *
     * @param images       Challenge tile images.
     * @param question     The challenge question.
     * @param questionType PopularCaptcha question type, e.g. {@code "objectClassify"}. Mandatory.
     * @param examples     Optional reference/example images (may be null).
     * @param screenshot   Whether the image is a full-page screenshot.
     * @param websiteUrl   Optional URL of the page with the CAPTCHA.
     * @param websiteKey   Optional PopularCaptcha site key.
     */
    public CreateTaskResponse solvePopularCaptcha(List<byte[]> images, String question,
            String questionType, List<byte[]> examples, boolean screenshot,
            String websiteUrl, String websiteKey) {
        Task.Builder b = Task.newBuilder()
                .setType("PopularCaptchaImage")
                .setQuestion(question)
                .setQuestionType(questionType)
                .setScreenshot(screenshot);
        toByteStrings(images).forEach(b::addImages);
        if (examples != null) toByteStrings(examples).forEach(b::addExamples);
        if (websiteUrl != null) b.setWebsiteURL(websiteUrl);
        if (websiteKey != null) b.setWebsiteKey(websiteKey);
        return createTask(b.build());
    }

    /**
     * Solve a reCAPTCHA v2 image classification challenge.
     *
     * <p>Task type: {@code RecaptchaV2Classification}
     *
     * @param images   Challenge tile images (typically 9 for 3x3 grid).
     * @param question Challenge category, e.g. {@code "traffic lights"} or {@code "/m/015qff"}.
     */
    public CreateTaskResponse solveRecaptchaV2(List<byte[]> images, String question) {
        return solveRecaptchaV2(images, question, null, null, null);
    }

    /**
     * Solve a reCAPTCHA v2 image challenge with questionType and analytics.
     *
     * @param images       Challenge tile images.
     * @param question     Challenge category text or class code.
     * @param questionType Grid type: {@code "split_33"}, {@code "33"}, {@code "44"}. May be null.
     * @param websiteUrl   Optional URL of the page with the CAPTCHA.
     * @param websiteKey   Optional reCAPTCHA site key.
     */
    public CreateTaskResponse solveRecaptchaV2(List<byte[]> images, String question,
            String questionType, String websiteUrl, String websiteKey) {
        Task.Builder b = Task.newBuilder().setType("RecaptchaV2Classification").setQuestion(question);
        toByteStrings(images).forEach(b::addImages);
        if (questionType != null) b.setQuestionType(questionType);
        if (websiteUrl != null) b.setWebsiteURL(websiteUrl);
        if (websiteKey != null) b.setWebsiteKey(websiteKey);
        return createTask(b.build());
    }

    /**
     * Solve a Geetest challenge with explicit question prefix.
     * Server routes click/slide/nine from the question prefix
     * ({@code "geetest_click:desc"}, {@code "geetest_slide"}, {@code "geetest_nine:desc"}).
     */
    public CreateTaskResponse solveGeetest(String question, List<byte[]> images) {
        return solveGeetest(question, images, null, null, 3);
    }

    /** Solve a Geetest challenge with full options.
     * @param websiteUrl Optional target website URL. Used to determine slide offset
     *                   (bingx.com uses 58px vs default 39px). Pass null if not needed.
     */
    public CreateTaskResponse solveGeetest(String question, List<byte[]> images,
            List<byte[]> examples, byte[] bgImage, int gtv, String websiteUrl) {
        Task.Builder b = Task.newBuilder()
                .setType("GeetestClassification")
                .setQuestion(question)
                .setGtv(gtv);
        if (images != null) toByteStrings(images).forEach(b::addImages);
        if (examples != null) toByteStrings(examples).forEach(b::addExamples);
        if (bgImage != null) b.setImage(ByteString.copyFrom(bgImage));
        if (websiteUrl != null) b.setWebsiteURL(websiteUrl);
        return createTask(b.build());
    }

    /** Solve a Geetest challenge with full options (no websiteUrl). */
    public CreateTaskResponse solveGeetest(String question, List<byte[]> images,
            List<byte[]> examples, byte[] bgImage, int gtv) {
        return solveGeetest(question, images, examples, bgImage, gtv, null);
    }


    /** Solve an AWS WAF challenge. */
    public CreateTaskResponse solveAwsWaf(List<byte[]> images, String question) {
        return solveAwsWaf(images, question, null, null);
    }

    /**
     * Solve an AWS WAF challenge with analytics.
     * @param websiteUrl Optional URL of the page.
     * @param websiteKey Optional site key.
     */
    public CreateTaskResponse solveAwsWaf(List<byte[]> images, String question,
            String websiteUrl, String websiteKey) {
        Task.Builder b = Task.newBuilder().setType("AwsWafClassification").setQuestion(question);
        toByteStrings(images).forEach(b::addImages);
        if (websiteUrl != null) b.setWebsiteURL(websiteUrl);
        if (websiteKey != null) b.setWebsiteKey(websiteKey);
        return createTask(b.build());
    }

    /** Solve an OCR / image-to-text challenge. */
    public CreateTaskResponse solveOcr(List<byte[]> images) {
        return solveOcr(images, null, false, false, 0, 0, null);
    }

    /** Solve an OCR challenge with full options. */
    public CreateTaskResponse solveOcr(List<byte[]> images, String module,
            boolean numeric, boolean caseSensitive, int minLength, int maxLength) {
        return solveOcr(images, module, numeric, caseSensitive, minLength, maxLength, null);
    }

    /**
     * Solve an OCR challenge with full options and analytics.
     * @param websiteUrl Optional URL of the page.
     */
    public CreateTaskResponse solveOcr(List<byte[]> images, String module,
            boolean numeric, boolean caseSensitive, int minLength, int maxLength,
            String websiteUrl) {
        Task.Builder b = Task.newBuilder()
                .setType("ImageToTextTask")
                .setNumeric(numeric)
                .setCaseSensitive(caseSensitive);
        toByteStrings(images).forEach(b::addImages);
        if (module != null) b.setModule(module);
        if (minLength > 0) b.setMinLength(minLength);
        if (maxLength > 0) b.setMaxLength(maxLength);
        if (websiteUrl != null) b.setWebsiteURL(websiteUrl);
        return createTask(b.build());
    }

    /**
     * Solve a TikTok challenge with type-based routing.
     *
     * @param type     {@code "click"}, {@code "whirl"}, or {@code "slide"}
     *                 (aliases like {@code "tiktok_click"} also accepted).
     * @param question What to find / action description.
     * @param images   Challenge images.
     */
    public CreateTaskResponse solveTikTok(String type, String question, List<byte[]> images) {
        return solveTikTok(type, question, images, null, null);
    }

    /**
     * Solve a TikTok challenge with examples and analytics.
     */
    public CreateTaskResponse solveTikTok(String type, String question, List<byte[]> images,
            List<byte[]> examples, String websiteUrl) {
        String normalized = normalizeTikTokType(type);
        String fullQuestion = "tiktok_" + normalized + ":" + (question != null ? question : "");
        Task.Builder b = Task.newBuilder().setType("tiktokClassification").setQuestion(fullQuestion);
        toByteStrings(images).forEach(b::addImages);
        if (examples != null) toByteStrings(examples).forEach(b::addExamples);
        if (websiteUrl != null) b.setWebsiteURL(websiteUrl);
        return createTask(b.build());
    }

    /** @deprecated Use {@link #solveTikTok(String, String, List)} with type param instead. */
    @Deprecated
    public CreateTaskResponse solveTikTokLegacy(String question, List<byte[]> images) {
        Task.Builder b = Task.newBuilder().setType("TikTokClassification").setQuestion(question);
        toByteStrings(images).forEach(b::addImages);
        return createTask(b.build());
    }

    /**
     * Solve a Binance challenge with type-based routing.
     *
     * @param type     {@code "grid"} or {@code "slide"}
     *                 (aliases like {@code "binance_grid"} also accepted).
     * @param question What to find. Required for {@code "grid"}.
     * @param images   Challenge images.
     */
    public CreateTaskResponse solveBinance(String type, String question, List<byte[]> images) {
        return solveBinance(type, question, images, null, null);
    }

    /**
     * Solve a Binance challenge with examples and analytics.
     */
    public CreateTaskResponse solveBinance(String type, String question, List<byte[]> images,
            List<byte[]> examples, String websiteUrl) {
        String normalized = normalizeBinanceType(type);
        String fullQuestion = "binance_" + normalized + ":" + (question != null ? question : "");
        Task.Builder b = Task.newBuilder().setType("BinanceClassification").setQuestion(fullQuestion);
        toByteStrings(images).forEach(b::addImages);
        if (examples != null) toByteStrings(examples).forEach(b::addExamples);
        if (websiteUrl != null) b.setWebsiteURL(websiteUrl);
        return createTask(b.build());
    }

    /**
     * Solve a Cloudflare Turnstile token challenge (polls until ready, up to 120s).
     *
     * <p>Task type: {@code AntiTurnstileTask} (with proxy) /
     * {@code AntiTurnstileTaskProxyless} (no proxy).
     *
     * @param websiteUrl Full URL of the page hosting the Turnstile widget.
     * @param websiteKey Turnstile sitekey, e.g. {@code "0x4AAAAAAA_dummyKey"}.
     */
    public CreateTaskResponse solveTurnstile(String websiteUrl, String websiteKey) {
        return solveTurnstile(websiteUrl, websiteKey, null);
    }

    /**
     * Solve a Cloudflare Turnstile token challenge with a proxy.
     *
     * @param proxy Proxy string. Format: {@code "http://user:pass@host:port"}.
     *              Required for {@code AntiTurnstileTask}. Pass {@code null} for proxyless.
     */
    public CreateTaskResponse solveTurnstile(String websiteUrl, String websiteKey, String proxy) {
        String taskType = (proxy != null && !proxy.isEmpty()) ? "AntiTurnstileTask" : "AntiTurnstileTaskProxyless";
        Task.Builder b = Task.newBuilder()
                .setType(taskType)
                .setWebsiteURL(websiteUrl)
                .setWebsiteKey(websiteKey);
        if (proxy != null && !proxy.isEmpty()) b.setProxy(proxy);
        return createTask(b.build());
    }

    /**
     * Solve a PopularCaptcha token challenge via browser automation (polls until ready).
     *
     * @param websiteUrl Full URL of the page with the PopularCaptcha widget.
     * @param websiteKey PopularCaptcha sitekey.
     * @param proxy Proxy string. {@code null} for proxyless.
     */
    public CreateTaskResponse solvePopularCaptchaToken(String websiteUrl, String websiteKey, String proxy) {
        return solvePopularCaptchaToken(websiteUrl, websiteKey, proxy, null);
    }

    /** Solve a PopularCaptcha token challenge with enterprise metadata. */
    public CreateTaskResponse solvePopularCaptchaToken(String websiteUrl, String websiteKey,
            String proxy, java.util.Map<String, String> metadata) {
        String taskType = (proxy != null && !proxy.isEmpty()) ? "PopularTask" : "PopularTaskProxyless";
        Task.Builder b = Task.newBuilder()
                .setType(taskType)
                .setWebsiteURL(websiteUrl)
                .setWebsiteKey(websiteKey);
        if (proxy != null && !proxy.isEmpty()) b.setProxy(proxy);
        return createTask(b.build());
    }

    /**
     * Solve a reCAPTCHA v2 token challenge via browser automation (polls until ready).
     *
     * <p>Task type: {@code RecaptchaV2Task} (with proxy) /
     * {@code RecaptchaV2TaskProxyless} (no proxy).
     *
     * @param websiteUrl Full URL of the page with the reCAPTCHA widget.
     * @param websiteKey reCAPTCHA v2 sitekey, e.g. {@code "6Le-wvkSAAAAAPBMRTvw0Q4Muexq9bi0DJwx_mJ-"}.
     * @param proxy Proxy string. {@code null} for proxyless.
     */
    public CreateTaskResponse solveRecaptchaV2Token(String websiteUrl, String websiteKey, String proxy) {
        String taskType = (proxy != null && !proxy.isEmpty()) ? "RecaptchaV2Task" : "RecaptchaV2TaskProxyless";
        Task.Builder b = Task.newBuilder()
                .setType(taskType)
                .setWebsiteURL(websiteUrl)
                .setWebsiteKey(websiteKey);
        if (proxy != null && !proxy.isEmpty()) b.setProxy(proxy);
        return createTask(b.build());
    }

    /**
     * Solve a reCAPTCHA v3 token challenge via browser automation (polls until ready).
     *
     * <p>Task type: {@code RecaptchaV3Task} (with proxy) /
     * {@code RecaptchaV3TaskProxyless} (no proxy).
     *
     * @param websiteUrl Full URL of the page with the reCAPTCHA v3 widget.
     * @param websiteKey reCAPTCHA v3 sitekey.
     * @param proxy Proxy string. {@code null} for proxyless.
     */
    public CreateTaskResponse solveRecaptchaV3Token(String websiteUrl, String websiteKey, String proxy) {
        String taskType = (proxy != null && !proxy.isEmpty()) ? "RecaptchaV3Task" : "RecaptchaV3TaskProxyless";
        Task.Builder b = Task.newBuilder()
                .setType(taskType)
                .setWebsiteURL(websiteUrl)
                .setWebsiteKey(websiteKey);
        if (proxy != null && !proxy.isEmpty()) b.setProxy(proxy);
        return createTask(b.build());
    }

    /**
     * Solve a Cloudflare challenge via browser automation (polls until ready).
     *
     * <p>Task type: {@code AntiCloudflareTask}. <b>Proxy is always required.</b>
     *
     * @param websiteUrl Full URL of the Cloudflare-protected page.
     * @param websiteKey Cloudflare sitekey.
     * @param proxy <b>Required.</b> Format: {@code "http://user:pass@host:port"}.
     */
    public CreateTaskResponse solveCloudflare(String websiteUrl, String websiteKey, String proxy) {
        Task.Builder b = Task.newBuilder()
                .setType("AntiCloudflareTask")
                .setWebsiteURL(websiteUrl)
                .setWebsiteKey(websiteKey)
                .setProxy(proxy);
        return createTask(b.build());
    }

    /**
     * Solve a slide CAPTCHA using local alpha/contour detection (no AI).
     *
     * <p>Task type: {@code SlideImage}. Billed as PopularCaptcha credits.
     *
     * <p>Pass 1–2 images: single image with transparent overlay, or {@code [background, piece]}.
     * Returns {@code TypedSolution.getSlide().getX()} as the pixel offset.
     */
    public CreateTaskResponse solveSlideImage(List<byte[]> images) {
        Task.Builder b = Task.newBuilder().setType("SlideImage");
        toByteStrings(images).forEach(b::addImages);
        return createTask(b.build());
    }


    /** Async variant of {@link #solvePopularCaptcha(List, String, String)}. */
    public CompletableFuture<CreateTaskResponse> solvePopularCaptchaAsync(
            List<byte[]> images, String question, String questionType) {
        return CompletableFuture.supplyAsync(() -> solvePopularCaptcha(images, question, questionType));
    }

    /** Async variant with examples, screenshot flag, and website context. */
    public CompletableFuture<CreateTaskResponse> solvePopularCaptchaAsync(
            List<byte[]> images, String question, String questionType,
            List<byte[]> examples, boolean screenshot, String websiteUrl, String websiteKey) {
        return CompletableFuture.supplyAsync(() ->
                solvePopularCaptcha(images, question, questionType, examples, screenshot, websiteUrl, websiteKey));
    }

    /** Async variant of {@link #solveRecaptchaV2(List, String)}. */
    public CompletableFuture<CreateTaskResponse> solveRecaptchaV2Async(
            List<byte[]> images, String question) {
        return CompletableFuture.supplyAsync(() -> solveRecaptchaV2(images, question));
    }

    /** Async variant of {@link #solveGeetest(String, List)}. */
    public CompletableFuture<CreateTaskResponse> solveGeetestAsync(
            String question, List<byte[]> images) {
        return CompletableFuture.supplyAsync(() -> solveGeetest(question, images));
    }


    /** Async variant of {@link #solveAwsWaf(List, String)}. */
    public CompletableFuture<CreateTaskResponse> solveAwsWafAsync(
            List<byte[]> images, String question) {
        return CompletableFuture.supplyAsync(() -> solveAwsWaf(images, question));
    }

    /** Async variant of {@link #solveOcr(List)}. */
    public CompletableFuture<CreateTaskResponse> solveOcrAsync(List<byte[]> images) {
        return CompletableFuture.supplyAsync(() -> solveOcr(images));
    }

    /** Async variant of {@link #solveOcr(List, String, boolean, boolean, int, int)}. */
    public CompletableFuture<CreateTaskResponse> solveOcrAsync(
            List<byte[]> images, String module, boolean numeric, boolean caseSensitive,
            int minLength, int maxLength) {
        return CompletableFuture.supplyAsync(
                () -> solveOcr(images, module, numeric, caseSensitive, minLength, maxLength));
    }

    /** Async variant of {@link #solveTikTok(String, String, List)}. */
    public CompletableFuture<CreateTaskResponse> solveTikTokAsync(
            String type, String question, List<byte[]> images) {
        return CompletableFuture.supplyAsync(() -> solveTikTok(type, question, images));
    }

    /** Async variant of {@link #solveBinance(String, String, List)}. */
    public CompletableFuture<CreateTaskResponse> solveBinanceAsync(
            String type, String question, List<byte[]> images) {
        return CompletableFuture.supplyAsync(() -> solveBinance(type, question, images));
    }

    /** Async variant of {@link #solveTurnstile(String, String)}. */
    public CompletableFuture<CreateTaskResponse> solveTurnstileAsync(
            String websiteUrl, String websiteKey) {
        return CompletableFuture.supplyAsync(() -> solveTurnstile(websiteUrl, websiteKey));
    }

    /** Async variant with proxy. */
    public CompletableFuture<CreateTaskResponse> solveTurnstileAsync(
            String websiteUrl, String websiteKey, String proxy) {
        return CompletableFuture.supplyAsync(() -> solveTurnstile(websiteUrl, websiteKey, proxy));
    }

    /** Async variant of {@link #solvePopularCaptchaToken(String, String, String)}. */
    public CompletableFuture<CreateTaskResponse> solvePopularCaptchaTokenAsync(
            String websiteUrl, String websiteKey, String proxy) {
        return CompletableFuture.supplyAsync(() -> solvePopularCaptchaToken(websiteUrl, websiteKey, proxy));
    }

    /** Async variant of {@link #solveRecaptchaV2Token(String, String, String)}. */
    public CompletableFuture<CreateTaskResponse> solveRecaptchaV2TokenAsync(
            String websiteUrl, String websiteKey, String proxy) {
        return CompletableFuture.supplyAsync(() -> solveRecaptchaV2Token(websiteUrl, websiteKey, proxy));
    }

    /** Async variant of {@link #solveRecaptchaV3Token(String, String, String)}. */
    public CompletableFuture<CreateTaskResponse> solveRecaptchaV3TokenAsync(
            String websiteUrl, String websiteKey, String proxy) {
        return CompletableFuture.supplyAsync(() -> solveRecaptchaV3Token(websiteUrl, websiteKey, proxy));
    }

    /** Async variant of {@link #solveCloudflare(String, String, String)}. */
    public CompletableFuture<CreateTaskResponse> solveCloudflareAsync(
            String websiteUrl, String websiteKey, String proxy) {
        return CompletableFuture.supplyAsync(() -> solveCloudflare(websiteUrl, websiteKey, proxy));
    }

    /** Async variant of {@link #solveSlideImage(List)}. */
    public CompletableFuture<CreateTaskResponse> solveSlideImageAsync(List<byte[]> images) {
        return CompletableFuture.supplyAsync(() -> solveSlideImage(images));
    }



    @Override
    public void close() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            channel.shutdownNow();
        }
    }


    private CreateTaskResponse pollTask(String taskId) {
        long deadline = System.currentTimeMillis() + POLL_MAX_WAIT_MS;
        while (System.currentTimeMillis() < deadline) {
            GetTaskResultResponse result = getTaskResult(taskId);
            if ("ready".equals(result.getStatus())) {
                return CreateTaskResponse.newBuilder()
                        .setTaskId(taskId)
                        .setStatus(result.getStatus())
                        .setTypedSolution(result.getTypedSolution())
                        .build();
            }
            try {
                Thread.sleep(POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CaptchaSonicException("Polling interrupted for task: " + taskId, e);
            }
        }
        throw new CaptchaSonicException("Task " + taskId + " did not complete within "
                + (POLL_MAX_WAIT_MS / 1000) + "s");
    }

    @FunctionalInterface
    private interface RpcCall<T> {
        T call() throws StatusRuntimeException;
    }

    private <T> T withRetry(RpcCall<T> fn) {
        RuntimeException last = null;
        for (int i = 0; i < RETRY_ATTEMPTS; i++) {
            try {
                return fn.call();
            } catch (StatusRuntimeException e) {
                switch (e.getStatus().getCode()) {
                    case UNAVAILABLE:
                    case RESOURCE_EXHAUSTED:
                    case DEADLINE_EXCEEDED:
                        if (i < RETRY_ATTEMPTS - 1) {
                            try {
                                Thread.sleep((long) Math.pow(2, i) * 1_000L);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                throw new CaptchaSonicException("Retry interrupted", ie);
                            }
                            last = e;
                            break;
                        }
                        // fall through
                    default:
                        throw new CaptchaSonicException(e.getStatus().getDescription(), e);
                }
            }
        }
        throw new CaptchaSonicException("Max retries exceeded", last);
    }

    private static List<ByteString> toByteStrings(List<byte[]> images) {
        return images.stream().map(ByteString::copyFrom).collect(Collectors.toList());
    }

    private static String normalizeTikTokType(String type) {
        return switch (type.toLowerCase()) {
            case "click", "tiktok_click" -> "click";
            case "whirl", "tiktok_whirl" -> "whirl";
            case "slide", "tiktok_slide" -> "slide";
            default -> type;
        };
    }

    private static String normalizeBinanceType(String type) {
        return switch (type.toLowerCase()) {
            case "grid", "binance_grid" -> "grid";
            case "slide", "binance_slide" -> "slide";
            default -> type;
        };
    }

    /** Validates API key format at construction time — no network call needed. */
    private static void validateApiKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new InvalidApiKeyException("apiKey is required");
        }
        if (!key.startsWith("sonic_")) {
            throw new InvalidApiKeyException("apiKey must start with \"sonic_\"");
        }
        if (key.length() < 12) {
            throw new InvalidApiKeyException("apiKey is too short");
        }
    }

    private static void throwForErrorId(int errorId, String message) {
        throw switch (errorId) {
            case 1 -> new InvalidApiKeyException(message);
            case 2 -> new InsufficientBalanceException(message);
            case 3 -> new DailyLimitExceededException(message);
            case 4 -> new MinuteLimitExceededException(message);
            case 5 -> new QuotaExceededException(message);
            case 6 -> new PlanExpiredException(message);
            default -> new CaptchaSonicException("Server error (errorId=" + errorId + "): " + message);
        };
    }
}
