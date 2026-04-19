package io.github.therepanic.funpay4j.exceptions.offer;

/**
 * Base class for exception related to the fact that the offer already raised
 *
 * @author therepanic
 * @since 1.0.3
 */
public class OfferAlreadyRaisedException extends RuntimeException {
    /**
     * Initializes a new OfferAlreadyRaisedException exception
     *
     * @param message exception message
     */
    public OfferAlreadyRaisedException(String message) {
        super(message);
    }
}
