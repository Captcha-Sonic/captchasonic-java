package com.captchasonic;

public class InvalidApiKeyException extends CaptchaSonicException {
    public InvalidApiKeyException(String message) { super(message); }
}
