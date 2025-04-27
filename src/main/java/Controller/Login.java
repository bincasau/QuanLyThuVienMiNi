package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login {
    private static final String mysql = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/IS216";
	 private static final String USER = "root";
	 private static final String PASSWORD = "";

    public interface LoginCallBack {
        void onSuccess(String username, boolean isAdmin);
        void onFailure(String message);
        void onError(String errorMessage);
    }

    public void login(String username, String password, LoginCallBack callBack) {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            callBack.onFailure("Vui lòng nhập đầy đủ thông tin đăng nhập");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String hasedPassword = hashPasswordSHA1(password);

            Class.forName(mysql);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "SELECT * FROM DocGia WHERE taiKhoan = ? AND matKhau = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hasedPassword);

            rs = stmt.executeQuery();

            if (rs.next()) {
                boolean isAdmin = "admin".equals(username);
                callBack.onSuccess(username, isAdmin);
            } else {
                callBack.onFailure("Tên đăng nhập hoặc mật khẩu không đúng");
            }
        } catch (ClassNotFoundException e) {
            callBack.onError("Không tìm thấy driver MySQL: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            callBack.onError("Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs == null) rs.close();
                if (stmt == null) stmt.close();
                if (conn == null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    String hashPasswordSHA1(String password) {
        return ""; // tao chua code xong
    }
}
