package Session;

public class LoginSession {
    private static LoginSession instance;
    private String fullName;
    private boolean isAdmin;

    private LoginSession() {}

    public static LoginSession getInstance() {
        if (instance == null) {
            instance = new LoginSession();
        }
        return instance;
    }

    public void login(String fullName, boolean isAdmin) {
        this.fullName = fullName;
        this.isAdmin = isAdmin;
    }

    public void logout() {
        this.fullName = null;
        this.isAdmin = false;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isLoggedIn() {
        return fullName != null;
    }
} 