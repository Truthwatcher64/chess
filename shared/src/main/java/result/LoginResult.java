package result;

/**
 * Result of a Login attempt
 */
public class LoginResult extends Result{
    /**
     * username of successful sign in
     */
    private String username;
    /**
     * authToken that is associated with the user
     */
    private String authToken;

    /**
     * Creates the Result, no parameters
     */
    public LoginResult(){

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
