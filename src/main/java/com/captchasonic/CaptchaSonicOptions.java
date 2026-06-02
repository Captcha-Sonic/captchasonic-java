package com.captchasonic;

import java.time.Duration;

/**
 * Configuration options for {@link CaptchaSonicClient}.
 *
 * <p>Use the builder pattern for idiomatic Java configuration:
 *
 * <pre>{@code
 * CaptchaSonicClient client = CaptchaSonicClient.builder()
 *     .apiKey("sonic_xxx")
 *     .host("api.captchasonic.com")
 *     .port(443)
 *     .timeout(Duration.ofSeconds(120))
 *     .build();
 * }</pre>
 */
public class CaptchaSonicOptions {

    private final String apiKey;
    private final String host;
    private final int port;
    private final boolean useTls;
    private final Duration timeout;

    CaptchaSonicOptions(Builder builder) {
        this.apiKey = builder.apiKey;
        this.host = builder.host;
        this.port = builder.port;
        this.useTls = builder.useTls;
        this.timeout = builder.timeout;
    }

    public String getApiKey() { return apiKey; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public boolean isUseTls() { return useTls; }
    public Duration getTimeout() { return timeout; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String apiKey;
        private String host = "api.captchasonic.com";
        private int port = 443;
        private boolean useTls = true;
        private Duration timeout = Duration.ofSeconds(120);

        /** Your CaptchaSonic API key (must start with {@code sonic_}). */
        public Builder apiKey(String apiKey) { this.apiKey = apiKey; return this; }

        /** gRPC host, e.g. {@code "api.captchasonic.com"}. Default: production. */
        public Builder host(String host) { this.host = host; return this; }

        /** gRPC port. Default: 443. */
        public Builder port(int port) { this.port = port; return this; }

        /** Whether to use TLS. Default: true. Set false for local dev. */
        public Builder useTls(boolean useTls) { this.useTls = useTls; return this; }

        /** Max poll wait for token tasks. Default: 120s. */
        public Builder timeout(Duration timeout) { this.timeout = timeout; return this; }

        /** Build the options and create a new {@link CaptchaSonicClient}. */
        public CaptchaSonicClient build() {
            CaptchaSonicOptions options = new CaptchaSonicOptions(this);
            return new CaptchaSonicClient(options);
        }
    }
}
