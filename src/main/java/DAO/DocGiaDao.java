package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.DocGia;
import Util.JDBCUtil;

public class DocGiaDao implements InterfaceDao<DocGia>{
    
    public static DocGiaDao getInstance() {
		return new DocGiaDao();
	}
    
    @Override
    public int themDoiTuong(DocGia t) {
        String sql = "INSERT INTO DocGia (maNguoiDung, taiKhoan, matKhau, email, soDienThoai, tenNguoiDung, ngayTao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getMaNguoiDung());
            stmt.setString(2, t.getTaiKhoan());
            stmt.setString(3, t.getMatKhau());
            stmt.setString(4, t.getEmail());
            stmt.setString(5, t.getSoDienThoai());
            stmt.setString(6, t.getTenNguoiDung());
            stmt.setDate(7, new Date(t.getNgayTao().getTime()));
            return stmt.executeUpdate(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; 
        }
    }

    public String layMaNguoiDungMoiNhat() {
        String sql = "SELECT maNguoiDung FROM DocGia ORDER BY maNguoiDung DESC LIMIT 1";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String lastMa = rs.getString("maNguoiDung");
                if (lastMa != null && lastMa.startsWith("DG") && lastMa.substring(2).matches("\\d+")) {
                    int number = Integer.parseInt(lastMa.replace("DG", "")) + 1;
                    return String.format("DG%03d", number);
                }
            }
            return "DG001"; // Mặc định nếu bảng rỗng hoặc mã không hợp lệ
        } catch (SQLException e) {
            e.printStackTrace();
            return "DG001";
        }
    }

    @Override
    public int xoaDoiTuong(DocGia t) {
        String sql = "DELETE FROM DocGia WHERE maNguoiDung = ?";
        try (Connection conn = JDBCUtil.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getMaNguoiDung());
            JDBCUtil.closeConnection();
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int capNhatDoiTuong(DocGia t) {
        String sql = "UPDATE DocGia SET taiKhoan = ?, matKhau = ?, email = ?, soDienThoai = ?, tenNguoiDung = ?, ngayTao = ? WHERE maNguoiDung = ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getTaiKhoan());
            stmt.setString(2, t.getMatKhau());
            stmt.setString(3, t.getEmail());
            stmt.setString(4, t.getSoDienThoai());
            stmt.setString(5, t.getTenNguoiDung());
            stmt.setDate(6, (Date) t.getNgayTao());
            stmt.setString(7, t.getMaNguoiDung());
            JDBCUtil.closeConnection();
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<DocGia> layDanhSach() {
        List<DocGia> list = new ArrayList<>();
        String sql = "SELECT maNguoiDung, tenNguoiDung, taiKhoan, matKhau, email, soDienThoai, ngayTao FROM DocGia";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                DocGia docGia = new DocGia(
                    rs.getString("maNguoiDung"),
                    rs.getString("tenNguoiDung"),
                    rs.getString("taiKhoan"),
                    rs.getString("matKhau"),
                    rs.getString("email"),
                    rs.getString("soDienThoai"),
                    rs.getDate("ngayTao")
                );
                list.add(docGia);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách độc giả: " + e.getMessage());
            e.printStackTrace();
        }
        JDBCUtil.closeConnection();
        return list;
    }

    public List<DocGia> timKiem(String keyword) {
        List<DocGia> list = new ArrayList<>();
        String sql = "SELECT * FROM DocGia WHERE maNguoiDung = ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
            stmt.setString(1, keyword);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DocGia docGia = new DocGia(
                        rs.getString("maNguoiDung"),
                        rs.getString("taiKhoan"),
                        rs.getString("matKhau"),
                        rs.getString("email"),
                        rs.getString("soDienThoai"),
                        rs.getString("tenNguoiDung"),
                        rs.getDate("ngayTao")
                    );
                    list.add(docGia);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JDBCUtil.closeConnection();
        return list;
    }
    public List<DocGia> layDanhSachTheoMa(String dk){
        List<DocGia> list = new ArrayList<>();
        String sql = "SELECT * FROM DocGia WHERE maNguoiDung LIKE ?";
             try(PreparedStatement stmt = JDBCUtil.connect().prepareStatement(sql)) {
            stmt.setString(1, dk);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DocGia docGia = new DocGia(
                        rs.getString("maNguoiDung"),
                        rs.getString("tenNguoiDung"),
                        rs.getString("taiKhoan"),
                        rs.getString("matKhau"),
                        rs.getString("email"),
                        rs.getString("soDienThoai"),
                        rs.getDate("ngayTao")
                    );
                    list.add(docGia);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm độc giả: " + e.getMessage());
            e.printStackTrace();
        }
        JDBCUtil.closeConnection();
        return list;
    }

    public int xoaDoiTuong(String maNguoiDung) {
        String sql = "DELETE FROM DocGia WHERE maNguoiDung = ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maNguoiDung);
            JDBCUtil.closeConnection();
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa độc giả: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

	@Override
	public List<DocGia> layDanhSachTheoDK(String dk) {
		// TODO Auto-generated method stub
		return null;
	}
}