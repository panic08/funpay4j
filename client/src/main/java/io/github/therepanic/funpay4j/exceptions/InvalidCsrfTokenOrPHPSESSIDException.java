package io.github.therepanic.funpay4j.exceptions;

/**
 * Base class for exception related to the fact that the csrf token or PHPSESSID is invalid
 *
 * @author therepanic
 * @since 1.0.3
 */
public class InvalidCsrfTokenOrPHPSESSIDException extends RuntimeException {
    /**
     * Initializes a new InvalidCsrfTokenOrPHPSESSIDException exception
     *
     * @param message exception message
     */
    public InvalidCsrfTokenOrPHPSESSIDException(String message) {
        super(message);
    }
}
