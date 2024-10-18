package exception;

public class NegativeSalaryException extends Exception {
    public NegativeSalaryException(String message) {
        super(message);
    }
}