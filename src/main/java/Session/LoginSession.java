package Session;

public class LoginSession {
    private static LoginSession instance;
    private String username;
    private boolean isAdmin;

    private LoginSession() {}

    public static LoginSession getInstance() {
        if (instance == null) {
            instance = new LoginSession();
        }
        return instance;
    }

    public void login(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public void logout() {
        this.username = null;
        this.isAdmin = false;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isLoggedIn() {
        return username != null;
    }
} 