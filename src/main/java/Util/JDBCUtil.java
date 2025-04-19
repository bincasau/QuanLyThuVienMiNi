package Util;

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
			System.out.println("helo util");
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
}
