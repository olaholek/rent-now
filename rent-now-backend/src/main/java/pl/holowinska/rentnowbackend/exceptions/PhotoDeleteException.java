package pl.holowinska.rentnowbackend.exceptions;

public class PhotoDeleteException extends Exception {

    public PhotoDeleteException() {
    }

    public PhotoDeleteException(String message) {
        super(message);
    }

    public PhotoDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhotoDeleteException(Throwable cause) {
        super(cause);
    }

    public PhotoDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
