package pl.holowinska.rentnowbackend.exceptions;

public class BookingConflictException extends Exception {

    public BookingConflictException() {
    }

    public BookingConflictException(String message) {
        super(message);
    }

    public BookingConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingConflictException(Throwable cause) {
        super(cause);
    }

    public BookingConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
