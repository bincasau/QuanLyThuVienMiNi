package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.Util.JDBCUtil; 

public class Login {
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
            String hashedPassword = JDBCUtil.hashPasswordSHA1(password);

            conn = JDBCUtil.connect();

            String sql = "SELECT * FROM DocGia WHERE taiKhoan = ? AND matKhau = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            rs = stmt.executeQuery();

            if (rs.next()) {
                boolean isAdmin = "admin".equals(username);
                callBack.onSuccess(username, isAdmin);
            } else {
                callBack.onFailure("Tên đăng nhập hoặc mật khẩu không đúng");
            }
        } catch (SQLException e) {
            callBack.onError("Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                JDBCUtil.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkUserExists(String username) throws Exception { // Kiểm tra khi thủ thư tạo tk mới cho độc giả, hoặc sửa username
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = Util.JDBCUtil.connect();

            String sql = "SELECT * FROM users WHERE username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();

            return rs.next();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
