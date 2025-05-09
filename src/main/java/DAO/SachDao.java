package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import Model.Sach;
import Model.TheLoai;
import Util.JDBCUtil;

public class SachDao implements InterfaceDao<Sach>{

	public static SachDao getInstance() {
		return new SachDao();
	}
	
	@Override
	public int themDoiTuong(Sach t) {
		int ketQua = 0;
		Connection conn = JDBCUtil.connect();
		String sql = "INSERT INTO sach(maSach, tenSach, nhaXB, namXB, gia, anh) VALUES (?, ?, ?, ?, ?, ?)";
		if (conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, t.getMaSach());
				stmt.setString(2, t.getTenSach());
				stmt.setString(3, t.getnXB());
				stmt.setInt(4, t.getNamXB());
				stmt.setDouble(5, t.getGia());
				stmt.setString(6, t.getAnh());

				ketQua = stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JDBCUtil.closeConnection();
		}
		return ketQua;
	}

	@Override
	public int xoaDoiTuong(Sach t) {
	    new LichSuMuonSachDao();
		LichSuMuonSachDao ls = LichSuMuonSachDao.getInstance();
	    
	    // Kiểm tra xem sách có đang mượn không
	    try {
	        if (ls.isBookCurrentlyBorrowed(t.getMaSach())) {
	            return 0; // Nếu sách đang mượn, không thể xóa
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return 0; // Nếu có lỗi khi kiểm tra trạng thái sách, trả về 0
	    }
	
	    int ketQua = 0;
	    Connection conn = JDBCUtil.connect();
	
	    String sqlXoaTheLoai = "DELETE FROM Sach_TheLoai WHERE maSach = ?";
	    String sqlXoaSach = "DELETE FROM Sach WHERE maSach = ?";
	    String sqlCapNhatLichSuMuon = "UPDATE LichSuMuonSach SET maSach = NULL WHERE maSach = ?";
	    String sqlCapNhatPhieuPhat = "UPDATE PhieuPhat SET maSach = NULL WHERE maSach = ?";
	
	    if (conn != null) {
	        try (PreparedStatement stmt1 = conn.prepareStatement(sqlXoaTheLoai);
	             PreparedStatement stmt2 = conn.prepareStatement(sqlCapNhatPhieuPhat);
	             PreparedStatement stmt3 = conn.prepareStatement(sqlCapNhatLichSuMuon);
	             PreparedStatement stmt4 = conn.prepareStatement(sqlXoaSach);) {
	
	            stmt1.setString(1, t.getMaSach());
	            stmt1.executeUpdate();
	
	            stmt2.setString(1,t.getMaSach());
	            stmt2.executeUpdate();
	            
	            stmt3.setString(1, t.getMaSach());
	            stmt3.executeUpdate();
	
	            stmt4.setString(1, t.getMaSach());
	            ketQua = stmt2.executeUpdate();
	
	            // Xóa ảnh
	            File imageFile = new File("Pictures/" + t.getAnh());
	            if (imageFile.exists()) {
	                if (imageFile.delete()) {
	                    System.out.println("Đã xóa ảnh: " + imageFile.getAbsolutePath());
	                } else {
	                    System.err.println("Không thể xóa ảnh: " + imageFile.getAbsolutePath());
	                }
	            }
	
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            JDBCUtil.closeConnection();
	        }
	    }
	
	    return ketQua;
	}

	@Override
	public int capNhatDoiTuong(Sach t) {
		int ketQua = 0;
		Connection conn = JDBCUtil.connect();
		String sql = "UPDATE sach SET tenSach = ?, nhaXB = ?, namXB = ?, gia = ?, anh = ? WHERE maSach = ?";
		if (conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, t.getTenSach());
				stmt.setString(2, t.getnXB());
				stmt.setInt(3, t.getNamXB());
				stmt.setDouble(4, t.getGia());
				stmt.setString(5, t.getAnh());
				stmt.setString(6, t.getMaSach());

				ketQua = stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JDBCUtil.closeConnection();
		}
		return ketQua;
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
// Lay theo ma
	@Override
	public List<Sach> layDanhSachTheoDK(String dk) {
		List<Sach> ds = new ArrayList<Sach>();
		Connection conn = JDBCUtil.connect();
		String sql = "select * from sach where maSach = ?";
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, dk);
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
	public String getLastBookCode() {
	    // Truy vấn lấy mã sách cuối cùng từ cơ sở dữ liệu
	    String sql = "SELECT maSach FROM sach ORDER BY maSach DESC LIMIT 1";
	    try (Connection conn = JDBCUtil.connect(); 
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        // Nếu có dữ liệu, lấy mã sách cuối cùng
	        if (rs.next()) {
	            String lastBookCode = rs.getString("maSach");
	            // Tách phần số từ mã sách
	            int lastCode = Integer.parseInt(lastBookCode.substring(1)); // Lấy phần số sau chữ "S"
	            return "S" + String.format("%04d", lastCode); // Tạo mã sách mới theo định dạng "S0000"
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    // Nếu không có sách nào trong cơ sở dữ liệu, trả về mã sách đầu tiên
	    return "S0001"; 
	}
	public void addCategoriesToBook(String maSach, List<TheLoai> dsTheLoai) {
	    Connection conn = JDBCUtil.connect();
	    String sql = "INSERT INTO sach_theloai(maSach, maTheLoai) VALUES (?, ?)";

	    if (conn != null) {
	        try {
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            for (TheLoai tl : dsTheLoai) {
	                stmt.setString(1, maSach);
	                stmt.setString(2, tl.getMaTheLoai());
	                stmt.addBatch();
	            }
	            stmt.executeBatch(); // Thực hiện nhiều insert một lúc
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            JDBCUtil.closeConnection();
	        }
	    }
	}
	public void updateBookAndCategories(Sach s) {
	    Connection conn = JDBCUtil.connect();
	    if (conn == null) return;

	    try {
	        conn.setAutoCommit(false); // Bắt đầu transaction

	        // 1. Cập nhật thông tin sách
	        String updateBookSql = "UPDATE sach SET tenSach = ?, nhaXB = ?, namXB = ?, gia = ?, anh = ? WHERE maSach = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(updateBookSql)) {
	            stmt.setString(1, s.getTenSach());
	            stmt.setString(2, s.getnXB());
	            stmt.setInt(3, s.getNamXB());
	            stmt.setDouble(4, s.getGia());
	            stmt.setString(5, s.getAnh());
	            stmt.setString(6, s.getMaSach());
	            stmt.executeUpdate();
	        }

	        // 2. Xóa thể loại cũ
	        String deleteOldCategoriesSql = "DELETE FROM sach_theloai WHERE maSach = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(deleteOldCategoriesSql)) {
	            stmt.setString(1, s.getMaSach());
	            stmt.executeUpdate();
	        }

	        // 3. Thêm thể loại mới
	        String insertCategorySql = "INSERT INTO sach_theloai(maSach, maTheLoai) VALUES (?, ?)";
	        try (PreparedStatement stmt = conn.prepareStatement(insertCategorySql)) {
	            for (TheLoai tl : s.getDsTheLoai()) {
	                stmt.setString(1, s.getMaSach());
	                stmt.setString(2, tl.getMaTheLoai());
	                stmt.addBatch(); // dùng batch insert cho hiệu quả
	            }
	            stmt.executeBatch();
	        }

	        conn.commit(); // Xác nhận thay đổi
	    } catch (SQLException e) {
	        try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
	        e.printStackTrace();
	    } finally {
	        JDBCUtil.closeConnection();
	    }
	}

}
