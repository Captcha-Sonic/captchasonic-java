package com.captchasonic;

public class MinuteLimitExceededException extends CaptchaSonicException {
    public MinuteLimitExceededException(String message) { super(message); }
}
