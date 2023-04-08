package ex.rr.adminpanel.exceptions;

import ex.rr.adminpanel.scheduler.TaskDefinition;

/**
 * The {@code UserInputException} class represents an exception that was directly caused ba user provided input.
 *
 * @author Robert Romanowicz
 */
public class UserInputException extends RuntimeException {

    public UserInputException(String message) {
        super(message);
    }
}
