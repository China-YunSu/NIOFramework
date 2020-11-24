package man.kuke.nio.core;

public class NoSetClientActionException extends RuntimeException{

    private static final long serialVersionUID = -8612936296481410151L;

    public NoSetClientActionException() {
        super();
    }

    public NoSetClientActionException(String message) {
        super(message);
    }

    public NoSetClientActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSetClientActionException(Throwable cause) {
        super(cause);
    }

    protected NoSetClientActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
