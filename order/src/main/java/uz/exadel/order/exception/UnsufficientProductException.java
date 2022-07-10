package uz.exadel.order.exception;


public class UnsufficientProductException extends RuntimeException {
    public UnsufficientProductException(String message) {
        super(message);
    }
}