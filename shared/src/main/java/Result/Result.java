package Result;

/**
 * Result from any type of service
 */
public class Result {
    /**
     * Potential error message to return after service
     */
    protected String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
