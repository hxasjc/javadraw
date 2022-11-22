package javadraw.errors;

/**
 * @deprecated Use {@link IllegalArgumentException} instead
 */
@Deprecated
public class InvalidArgumentException extends Exception {

    private String message;

    /**
     * New Exception
     * @param message Exception message
     */
    public InvalidArgumentException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
