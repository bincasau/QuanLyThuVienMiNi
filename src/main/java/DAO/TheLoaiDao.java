package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Model.TheLoai;
import Util.JDBCUtil;

public class TheLoaiDao implements InterfaceDao<TheLoai>{

	public static TheLoaiDao getInstance() {
		return new TheLoaiDao();
	}
	
	@Override
	public int themDoiTuong(TheLoai t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int xoaDoiTuong(TheLoai t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int capNhatDoiTuong(TheLoai t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<TheLoai> layDanhSach() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TheLoai> layDanhSachTheoDK(String dk) {
		List<TheLoai> ds = new ArrayList<TheLoai>();
		String sql = "select * from theloai as tl "
				+ "join sach_theloai s_tl on s_tl.maTheLoai = tl.maTheLoai "
				+ "where maSach = ?";
		Connection conn = JDBCUtil.connect();
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, dk);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					TheLoai tl = new TheLoai(rs.getString("maTheLoai"), rs.getString("tenTheLoai"));
					ds.add(tl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			JDBCUtil.closeConnection();
		}
		return ds;
	}

}
