package io.github.therepanic.funpay4j.exceptions.lot;

/**
 * Base class for exception related to the fact that the lot is not found
 *
 * @author therepanic
 * @since 1.0.3
 */
public class LotNotFoundException extends RuntimeException {
    /**
     * Initializes a new LotNotFoundException exception
     *
     * @param message exception message
     */
    public LotNotFoundException(String message) {
        super(message);
    }
}
