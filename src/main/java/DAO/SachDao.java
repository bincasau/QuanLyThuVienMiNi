package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Sach;
import Util.JDBCUtil;

public class SachDao implements InterfaceDao<Sach>{

	public static SachDao getInstance() {
		return new SachDao();
	}
	
	@Override
	public int themDoiTuong(Sach t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int xoaDoiTuong(Sach t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int capNhatDoiTuong(Sach t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Sach> layDanhSach() {
		List<Sach> ds = new ArrayList<Sach>();
		Connection conn = JDBCUtil.connect();
		String sql = "select * from sach";
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					Sach s = new Sach(rs.getString("maSach"),
									rs.getString("tenSach"), 
									rs.getString("nhaXB"), 
									rs.getInt("namXB"), 
									rs.getDouble("gia"), 
									rs.getString("anh"));
					s.setDsTheLoai(TheLoaiDao.getInstance().layDanhSachTheoDK(rs.getString("maSach")));
					ds.add(s);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JDBCUtil.closeConnection();
		}
		return ds;
	}

	@Override
	public List<Sach> layDanhSachTheoDK(String dk) {
		// TODO Auto-generated method stub
		return null;
	}

}
