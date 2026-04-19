package io.github.therepanic.funpay4j.exceptions.order;

/**
 * Base class for exception related to the fact that the order is not found
 *
 * @author therepanic
 * @since 1.0.7
 */
public class OrderNotFoundException extends RuntimeException {

    /**
     * Initializes a new OrderNotFoundException exception
     *
     * @param message exception message
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}
