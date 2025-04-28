package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.LichSuMuonSach;
import Util.JDBCUtil;

public class LichSuMuonSachDao implements InterfaceDao<LichSuMuonSach>{

	public static LichSuMuonSachDao getInstance() {
		return new LichSuMuonSachDao();
	}
	
	@Override
	public int themDoiTuong(LichSuMuonSach t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int xoaDoiTuong(LichSuMuonSach t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int capNhatDoiTuong(LichSuMuonSach t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<LichSuMuonSach> layDanhSach() {
        List<LichSuMuonSach> list = new ArrayList<>();
        String sql = "SELECT * FROM lichsumuonsach";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LichSuMuonSach ls = new LichSuMuonSach(
                        rs.getString("maLichSu"),
                        rs.getDate("ngayMuon"),
                        rs.getDate("ngayTra"),
                        rs.getString("trangThai"),
                        rs.getString("maSach"),
                        rs.getString("maThuThu"),
                        rs.getString("maDocGia")
                    );
                    list.add(ls);
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
	}

	@Override
	public List<LichSuMuonSach> layDanhSachTheoDK(String dk) {
		// TODO Auto-generated method stub
		return null;
	}

}
