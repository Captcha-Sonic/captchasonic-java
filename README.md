# CaptchaSonic

Official Java SDK for the [CaptchaSonic](https://captchasonic.com) CAPTCHA solving API.

## Transport

Uses native gRPC via `grpc-netty-shaded`. Images are sent as raw binary — no base64 overhead.

## Installation

### Gradle

```groovy
implementation 'com.captchasonic:captchasonic:1.0.0'
```

### Maven

```xml
<dependency>
    <groupId>com.captchasonic</groupId>
    <artifactId>captchasonic</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

```java
import com.captchasonic.CaptchaSonicClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

try (var client = new CaptchaSonicClient("sonic_your_api_key")) {
    double balance = client.getBalance();
    System.out.println("Balance: $" + balance);

    byte[] tile = Files.readAllBytes(Path.of("tile.png"));
    var result = client.solvePopularCaptcha(
        List.of(tile), "Select all traffic lights", "objectClassify"
    );
    System.out.println(result.getTypedSolution().getGrid().getObjectsList());
}
```

### Builder Pattern

```java
CaptchaSonicClient client = CaptchaSonicClient.builder()
    .apiKey("sonic_xxx")
    .host("api.captchasonic.com")
    .port(443)
    .timeout(Duration.ofSeconds(120))
    .build();
```

### Custom Endpoint

```java
var client = new CaptchaSonicClient("sonic_xxx", "api.captchasonic.com", 443, true);
```

The client is **thread-safe** — create one instance and reuse it.

---

## Image Solvers

### `solvePopularCaptcha()`

```java
client.solvePopularCaptcha(images, question, questionType);

// Full options
client.solvePopularCaptcha(
    images,                          // List<byte[]>
    "Select all traffic lights",     // question
    "objectClassify",                // questionType
    exampleImages,                   // List<byte[]>, nullable
    false,                           // screenshot
    "https://example.com",           // websiteUrl, nullable
    "sitekey123"                     // websiteKey, nullable
);
```

### `solveRecaptchaV2()`

```java
client.solveRecaptchaV2(images, "traffic lights");

// With questionType + options
client.solveRecaptchaV2(
    images,                   // List<byte[]>
    "traffic lights",         // question
    "split_33",               // questionType: "split_33" | "33" | "44"
    "https://example.com",    // websiteUrl, nullable
    null                      // websiteKey, nullable
);
```

### `solveGeetest()`

```java
client.solveGeetest("geetest_nine:Select the bear", images);

// Full options
client.solveGeetest(question, images, examples, bgImage, gtv, websiteUrl);
```

### `solveOcr()`

```java
client.solveOcr(images);

// With options
client.solveOcr(images, "mtcaptcha", false, false, 3, 4, "https://example.com");
```

### `solveTikTok()`

```java
// Type-based routing (alias expansion built-in)
client.solveTikTok("whirl", "Rotate to match", images);
client.solveTikTok("click", "Select the shape", images, examples, websiteUrl);
// Accepted types: "click", "whirl", "slide", "tiktok_click", etc.
```

### `solveBinance()`

```java
client.solveBinance("grid", "Select the bicycle", images);
client.solveBinance("slide", "", images, examples, websiteUrl);
// Accepted types: "grid", "slide", "binance_grid", "binance_slide"
```

### `solveAwsWaf()`

```java
client.solveAwsWaf(images, "grid:vehicles:cars");
client.solveAwsWaf(images, question, websiteUrl, websiteKey);
```

### `solveSlideImage()`

```java
client.solveSlideImage(List.of(background, piece));
// Returns: result.getTypedSolution().getSlide().getX()
```

---

## Token Automation Methods

These submit a task and poll until a token is returned (up to 120s).

```java
client.solveTurnstile(websiteUrl, websiteKey);
client.solveTurnstile(websiteUrl, websiteKey, proxy);
client.solvePopularCaptchaToken(websiteUrl, websiteKey, proxy);
client.solveRecaptchaV2Token(websiteUrl, websiteKey, proxy);
client.solveRecaptchaV3Token(websiteUrl, websiteKey, proxy);
client.solveCloudflare(websiteUrl, websiteKey, proxy);  // proxy required
```

Every blocking method has an `Async` variant returning `CompletableFuture<CreateTaskResponse>`.

---

## Utility Methods

```java
double balance = client.getBalance();
HealthCheckResponse health = client.healthCheck();
GetTaskResultResponse result = client.getTaskResult(taskId);
```

---

## Error Handling

```java
import com.captchasonic.*;

try {
    client.solvePopularCaptcha(images, question, questionType);
} catch (InvalidApiKeyException e)       { /* errorId=1 */ }
  catch (InsufficientBalanceException e) { /* errorId=2 */ }
  catch (DailyLimitExceededException e)  { /* errorId=3 */ }
  catch (MinuteLimitExceededException e) { /* errorId=4 */ }
  catch (QuotaExceededException e)       { /* errorId=5 */ }
  catch (PlanExpiredException e)         { /* errorId=6 */ }
  catch (CaptchaSonicException e)        { /* other errors */ }
```

Retries on `UNAVAILABLE`, `RESOURCE_EXHAUSTED`, `DEADLINE_EXCEEDED` with exponential backoff.

---

## Publishing to Maven Central

```bash
# Build
./gradlew build

# Publish (requires SONATYPE_USERNAME, SONATYPE_PASSWORD, SIGNING_KEY, SIGNING_PASSWORD)
./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
```

See the full [Maven Central Publish Guide](../../docs/java-publish-guide.md) for detailed steps.
