package exceptions;

public class InvalidInputFormatException extends AccountException {
    public InvalidInputFormatException(String message) {
        super(message);
    }
}