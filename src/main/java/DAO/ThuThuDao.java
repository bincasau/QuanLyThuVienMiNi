package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Model.ThuThu;
import Util.JDBCUtil;

public class ThuThuDao implements InterfaceDao<ThuThu>{

	public static ThuThuDao getInstance() {
		return new ThuThuDao();
	}
	
	@Override
	public int themDoiTuong(ThuThu t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int xoaDoiTuong(ThuThu t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int capNhatDoiTuong(ThuThu t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ThuThu> layDanhSach() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
public List<ThuThu> layDanhSachTheoDK(String dk) {
    List<ThuThu> ds = new ArrayList<ThuThu>();
    Connection conn = JDBCUtil.connect();
    String sql = "select * from thuthu where tenNguoiDung = ?";
    if (conn != null) {
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dk);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ThuThu tt = new ThuThu();
                tt.setMaNguoiDung(rs.getString("maNguoiDung"));
                tt.setTenNguoiDung(rs.getString("tenNguoiDung"));
                tt.setTaiKhoan(rs.getString("taiKhoan"));
                tt.setMatKhau(rs.getString("matKhau"));
                tt.setEmail(rs.getString("email"));
                tt.setSoDienThoai(rs.getString("soDienThoai"));
                ds.add(tt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBCUtil.closeConnection();
    }
    return ds;
}

}
