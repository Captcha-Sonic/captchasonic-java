package com.captchasonic;

public class QuotaExceededException extends CaptchaSonicException {
    public QuotaExceededException(String message) { super(message); }
}
