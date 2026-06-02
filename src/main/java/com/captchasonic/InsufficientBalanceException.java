package com.captchasonic;

public class InsufficientBalanceException extends CaptchaSonicException {
    public InsufficientBalanceException(String message) { super(message); }
}
