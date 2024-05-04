package Request;
/**
 * Request sent to register a new user
 */
public class RegisterRequest {
    /**
     * Username of a brand-new player
     */
    private String username;
    /**
     * Password of the new player
     */
    private String password;
    /**
     * Email of the new player
     */
    private String email;

    /**
     * Request for a new user
     */
    public RegisterRequest(){

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
