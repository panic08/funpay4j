package io.github.therepanic.funpay4j.exceptions.offer;

/**
 * Base class for exception related to the fact that the offer is not found
 *
 * @author therepanic
 * @since 1.0.3
 */
public class OfferNotFoundException extends RuntimeException {
    /**
     * Initializes a new OfferNotFoundException exception
     *
     * @param message exception message
     */
    public OfferNotFoundException(String message) {
        super(message);
    }
}
