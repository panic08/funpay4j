package io.github.therepanic.funpay4j.exceptions;

/**
 * Base class for exceptions related to FunPay
 *
 * @author therepanic
 * @since 1.0.0
 */
public class FunPayApiException extends Exception {
    /**
     * Initializes a new FunPayApiException exception
     *
     * @param message exception message
     */
    public FunPayApiException(String message) {
        super(message);
    }
}
