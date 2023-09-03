package pl.holowinska.rentnowbackend.exceptions;

public class AccommodationNotFoundException extends Exception {

    public AccommodationNotFoundException() {
    }

    public AccommodationNotFoundException(String message) {
        super(message);
    }

    public AccommodationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccommodationNotFoundException(Throwable cause) {
        super(cause);
    }

    public AccommodationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
