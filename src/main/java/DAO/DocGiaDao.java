package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.DocGia;

public class DocGiaDao implements InterfaceDao<DocGia> {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/IS216"; 
    private static final String DB_USER = "root"; // username MySQL
    private static final String DB_PASS = ""; // password MySQL

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
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
    public List<DocGia> layDanhSach() {
        List<DocGia> list = new ArrayList<>();
        String sql = "SELECT * FROM DocGia";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<DocGia> layDanhSachTheoDK(String dk) {
        List<DocGia> list = new ArrayList<>();
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
}
