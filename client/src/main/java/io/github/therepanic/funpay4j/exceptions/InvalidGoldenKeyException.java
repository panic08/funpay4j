package io.github.therepanic.funpay4j.exceptions;

/**
 * Base class for exception related to the fact that the goldenKey is invalid
 *
 * @author therepanic
 * @since 1.0.3
 */
public class InvalidGoldenKeyException extends RuntimeException {
    /**
     * Initializes a new InvalidGoldenKeyException exception
     *
     * @param message exception message
     */
    public InvalidGoldenKeyException(String message) {
        super(message);
    }
}
