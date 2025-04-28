package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Ls_Dg_sach;
import Util.JDBCUtil;

public class Ls_Dg_sachDao {
	
	public static Ls_Dg_sachDao getInstance() {
		return new Ls_Dg_sachDao();
	}
	public List<Ls_Dg_sach> layDanhSach() {
        List<Ls_Dg_sach> list = new ArrayList<>();
        String sql = "SELECT dg.maNguoiDung, tenNguoiDung, sc.maSach, tenSach, maLichSu, ngayMuon, ngayTra, trangThai, anh from lichsumuonsach ls join sach sc on ls.maSach = sc.maSach join docgia dg on ls.maDocGia = dg.maNguoiDung";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ls_Dg_sach ls = new Ls_Dg_sach(
                        rs.getString("maNguoiDung"),
                        rs.getString("tenNguoiDung"),
                        rs.getString("maSach"),
                        rs.getString("tenSach"),
                        rs.getString("maLichSu"),
                        rs.getDate("ngayMuon"),
                        rs.getDate("ngayTra"),
                        rs.getString("trangThai"),
                        rs.getString("anh")
                    );
                    
                    list.add(ls);
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBCUtil.closeConnection();
        return list;
	}

}
