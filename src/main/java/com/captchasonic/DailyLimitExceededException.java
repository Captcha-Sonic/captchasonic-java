package com.captchasonic;

public class DailyLimitExceededException extends CaptchaSonicException {
    public DailyLimitExceededException(String message) { super(message); }
}
