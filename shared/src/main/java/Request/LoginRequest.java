package Request;
/**
 * Request sent to login a user
 */
public class LoginRequest {
    /**
     * Username of the current user
     */
    private String username;
    /**
     * Attempted password for the user
     */
    private String password;

    /**
     * Makes request based on username and password
     */
    public LoginRequest(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
