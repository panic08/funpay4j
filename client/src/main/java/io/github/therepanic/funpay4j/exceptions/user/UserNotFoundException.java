package io.github.therepanic.funpay4j.exceptions.user;

/**
 * Base class for exception related to the fact that the user is not found
 *
 * @author therepanic
 * @since 1.0.3
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Initializes a new UserNotFoundException exception
     *
     * @param message exception message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
