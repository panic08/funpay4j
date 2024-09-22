package ru.funpay4j.core.exceptions;

/**
 * Base class for any exception from FunPayClient
 *
 * @author panic08
 * @since 1.0.0
 */
public class FunPayApiException extends RuntimeException {
    public FunPayApiException(String message) {
        super(message);
    }
}
