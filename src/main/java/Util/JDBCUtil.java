package Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	private static final String URL = "jdbc:mysql://localhost:3306/IS216";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	private static Connection c = null;
	public static Connection connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public static void closeConnection() {
		if(c != null) {
			try {
				c.close();
				c = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String hashPasswordSHA1(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] hashBytes = md.digest(password.getBytes());
		StringBuilder sb =  new StringBuilder();
		for (byte b: hashBytes) {
			sb.append(String.format("%02x", b));
		}
        return sb.toString(); 
    }
}
