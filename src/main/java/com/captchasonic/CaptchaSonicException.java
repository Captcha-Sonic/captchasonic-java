package com.captchasonic;

/** Base exception for all CaptchaSonic client errors. */
public class CaptchaSonicException extends RuntimeException {
    public CaptchaSonicException(String message) { super(message); }
    public CaptchaSonicException(String message, Throwable cause) { super(message, cause); }
}
