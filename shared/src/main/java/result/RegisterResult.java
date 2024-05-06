package result;
/**
 * Result from a attempted register of a new user
 */
public class RegisterResult extends Result{
    /**
     * Newly created username
     */
    private String username;
    /**
     * authToken associated with the new user
     */
    private String authToken;

    /**
     * Creates the result, no parameters
     */
    public RegisterResult(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
