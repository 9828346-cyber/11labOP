package exceptions;

public class NegativeBalanceException extends AccountException {
    public NegativeBalanceException(String message) {
        super(message);
    }
}