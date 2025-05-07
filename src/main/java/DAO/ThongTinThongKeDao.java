package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Model.ThongTinThongKe;
import Util.JDBCUtil;

public class ThongTinThongKeDao {

	public static ThongTinThongKeDao getInstance() {
		return new ThongTinThongKeDao();
	}
	
	public List<ThongTinThongKe> layDanhSachTheoNam(int nam){
		List<ThongTinThongKe> ds = new ArrayList<ThongTinThongKe>();
		Connection conn = JDBCUtil.connect();
		// Lấy ngày hiện tại để xác định năm và tháng hiện tại
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        StringBuilder sql = new StringBuilder();
        sql.append("WITH DocGiaThang AS (").append("\n");
        sql.append("    SELECT").append("\n");
        sql.append("        MONTH(ngayTao) AS thang,").append("\n");
        sql.append("        COUNT(*) AS soDocGiaDangKyMoi").append("\n");
        sql.append("    FROM DocGia").append("\n");
        sql.append("    WHERE YEAR(ngayTao) = ?").append("\n");
        sql.append("    AND ngayTao <= CASE").append("\n");
        sql.append("        WHEN ? = ").append(currentYear).append(" THEN CURDATE()").append("\n");
        sql.append("        ELSE CONCAT(?, '-12-31')").append("\n");
        sql.append("    END").append("\n");
        sql.append("    GROUP BY MONTH(ngayTao)").append("\n");
        sql.append("),").append("\n");
        sql.append("PhieuPhatThang AS (").append("\n");
        sql.append("    SELECT").append("\n");
        sql.append("        MONTH(ngayPhieu) AS thang,").append("\n");
        sql.append("        SUM(CASE WHEN loi = 'Trả sách trễ' THEN 1 ELSE 0 END) AS soLuongTraSachTre,").append("\n");
        sql.append("        SUM(CASE WHEN loi = 'Làm mất sách' THEN 1 ELSE 0 END) AS soLuongLamMatSach,").append("\n");
        sql.append("        SUM(CASE WHEN loi = 'Làm hư sách' THEN 1 ELSE 0 END) AS soLuongLamHuSach,").append("\n");
        sql.append("        SUM(giaTien) AS tongGiaTien").append("\n");
        sql.append("    FROM phieuphat").append("\n");
        sql.append("    WHERE YEAR(ngayPhieu) = ?").append("\n");
        sql.append("    AND ngayPhieu <= CASE").append("\n");
        sql.append("        WHEN ? = ").append(currentYear).append(" THEN CURDATE()").append("\n");
        sql.append("        ELSE CONCAT(?, '-12-31')").append("\n");
        sql.append("    END").append("\n");
        sql.append("    GROUP BY MONTH(ngayPhieu)").append("\n");
        sql.append(")").append("\n");
        sql.append("SELECT").append("\n");
        sql.append("    m.thang,").append("\n");
        sql.append("    COALESCE(d.soDocGiaDangKyMoi, 0) AS soDocGiaDangKyMoi,").append("\n");
        sql.append("    COALESCE(p.soLuongTraSachTre, 0) AS soLuongTraSachTre,").append("\n");
        sql.append("    COALESCE(p.soLuongLamMatSach, 0) AS soLuongLamMatSach,").append("\n");
        sql.append("    COALESCE(p.soLuongLamHuSach, 0) AS soLuongLamHuSach,").append("\n");
        sql.append("    COALESCE(p.tongGiaTien, 0) + COALESCE(d.soDocGiaDangKyMoi, 0) * 50000 AS doanhThu").append("\n");
        sql.append("FROM (").append("\n");
        // Tạo danh sách tháng động dựa trên năm
        int maxMonths = (nam == currentYear) ? currentMonth : 12;
        sql.append("    SELECT 1 AS thang");
        for (int i = 2; i <= maxMonths; i++) {
            sql.append(" UNION SELECT ").append(i);
        }
        sql.append(") m").append("\n");
        sql.append("LEFT JOIN DocGiaThang d ON m.thang = d.thang").append("\n");
        sql.append("LEFT JOIN PhieuPhatThang p ON m.thang = p.thang").append("\n");
        sql.append("ORDER BY m.thang;");
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement(sql.toString());
				stmt.setInt(1, nam); 
	            stmt.setInt(2, nam); 
	            stmt.setInt(3, nam); 
	            stmt.setInt(4, nam);
	            stmt.setInt(5, nam); 
	            stmt.setInt(6, nam); 
	            ResultSet rs = stmt.executeQuery();
	            while(rs.next()) {
	            	ThongTinThongKe tttk = new ThongTinThongKe(rs.getInt("thang"), 
	            				rs.getInt("soDocGiaDangKyMoi"), 
	            				rs.getInt("soLuongTraSachTre"), 
	            				rs.getInt("soLuongLamMatSach"), 
	            				rs.getInt("soLuongLamHuSach"), 
	            				rs.getDouble("doanhThu"));
	            	ds.add(tttk);
	            }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JDBCUtil.closeConnection();
		}
		return ds;
	}

}
