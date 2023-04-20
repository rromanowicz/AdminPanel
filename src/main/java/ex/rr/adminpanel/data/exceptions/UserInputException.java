package ex.rr.adminpanel.data.exceptions;

/**
 * The {@code UserInputException} class represents an exception that was directly caused ba user provided input.
 *
 * @author  rromanowicz
 */
public class UserInputException extends RuntimeException {

    public UserInputException(String message) {
        super(message);
    }
}
