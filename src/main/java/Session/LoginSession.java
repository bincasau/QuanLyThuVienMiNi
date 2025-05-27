package Session;

public class LoginSession {
    private static LoginSession instance;
    private String fullName;
    private boolean isAdmin;
    private String maNguoiDung;

    private LoginSession() {}

    public static LoginSession getInstance() {
        if (instance == null) {
            instance = new LoginSession();
        }
        return instance;
    }

    public void login(String fullName, String maNguoiDung, boolean isAdmin) {
        this.fullName = fullName;
        this.maNguoiDung = maNguoiDung;
        this.isAdmin = isAdmin;
    }

    public void logout() {
        this.fullName = null;
        this.maNguoiDung = null;
        this.isAdmin = false;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMaNguoiDung() { 
        return maNguoiDung;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isLoggedIn() {
        return fullName != null;
    }
} 