package man.kuke.nio.core;

public class NoSuchClientException extends Exception{
    private static final long serialVersionUID = 8380026363700089965L;

    public NoSuchClientException() {
        super();
    }

    public NoSuchClientException(String message) {
        super(message);
    }

    public NoSuchClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchClientException(Throwable cause) {
        super(cause);
    }

    protected NoSuchClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
