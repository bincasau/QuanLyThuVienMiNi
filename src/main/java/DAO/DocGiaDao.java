package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.DocGia;

public class DocGiaDao {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/IS216?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver không tìm thấy: " + e.getMessage());
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
<<<<<<< HEAD
    
    public static DocGiaDao getInstance() {
		return new DocGiaDao();
	}
    
    @Override
    public int themDoiTuong(DocGia t) {
        String sql = "INSERT INTO DocGia (maNguoiDung, taiKhoan, matKhau, email, soDienThoai, tenNguoiDung, ngayTao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getMaNguoiDung());
            stmt.setString(2, t.getTaiKhoan());
            stmt.setString(3, t.getMatKhau());
            stmt.setString(4, t.getEmail());
            stmt.setString(5, t.getSoDienThoai());
            stmt.setString(6, t.getTenNguoiDung());
            stmt.setDate(7, t.getNgayTao());

            return stmt.executeUpdate(); // trả về số dòng bị ảnh hưởng
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // thất bại
        }
    }

    @Override
    public int xoaDoiTuong(DocGia t) {
        String sql = "DELETE FROM DocGia WHERE maNguoiDung = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getMaNguoiDung());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int capNhatDoiTuong(DocGia t) {
        String sql = "UPDATE DocGia SET taiKhoan = ?, matKhau = ?, email = ?, soDienThoai = ?, tenNguoiDung = ?, ngayTao = ? WHERE maNguoiDung = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getTaiKhoan());
            stmt.setString(2, t.getMatKhau());
            stmt.setString(3, t.getEmail());
            stmt.setString(4, t.getSoDienThoai());
            stmt.setString(5, t.getTenNguoiDung());
            stmt.setDate(6, t.getNgayTao());
            stmt.setString(7, t.getMaNguoiDung());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
=======

>>>>>>> branch 'main' of https://github.com/NguyenVu3105/DoAnIS216.git
    public List<DocGia> layDanhSach() {
        List<DocGia> list = new ArrayList<>();
        String sql = "SELECT maNguoiDung, tenNguoiDung, taiKhoan, matKhau, email, soDienThoai, ngayTao FROM DocGia";
        try (Connection conn = getConnection();
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
        return list;
    }

    public List<DocGia> timKiem(String keyword) {
        List<DocGia> list = new ArrayList<>();
<<<<<<< HEAD
        String sql = "SELECT * FROM DocGia WHERE maNguoiDung = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
            stmt.setString(1, dk);

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
        return list;
    }
    public List<DocGia> layDanhSachTheoMa(String dk){
        List<DocGia> list = new ArrayList<>();
        String sql = "SELECT * FROM DocGia WHERE maNguoiDung LIKE ?";
=======
        String sql = "SELECT maNguoiDung, tenNguoiDung, taiKhoan, matKhau, email, soDienThoai, ngayTao FROM DocGia WHERE tenNguoiDung LIKE ?";
>>>>>>> branch 'main' of https://github.com/NguyenVu3105/DoAnIS216.git
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
<<<<<<< HEAD

            stmt.setString(1, dk);

=======
            stmt.setString(1, "%" + keyword + "%");
>>>>>>> branch 'main' of https://github.com/NguyenVu3105/DoAnIS216.git
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
        return list;
    }

    public int themDoiTuong(DocGia t) {
        String sql = "INSERT INTO DocGia (maNguoiDung, tenNguoiDung, taiKhoan, matKhau, email, soDienThoai, ngayTao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getMaNguoiDung());
            stmt.setString(2, t.getTenNguoiDung());
            stmt.setString(3, t.getTaiKhoan());
            stmt.setString(4, t.getMatKhau());
            stmt.setString(5, t.getEmail());
            stmt.setString(6, t.getSoDienThoai());
            stmt.setDate(7, new java.sql.Date(t.getNgayTao().getTime()));
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm độc giả: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public int xoaDoiTuong(String maNguoiDung) {
        String sql = "DELETE FROM DocGia WHERE maNguoiDung = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maNguoiDung);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa độc giả: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}