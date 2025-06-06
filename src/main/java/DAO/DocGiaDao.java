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

public class DocGiaDao implements InterfaceDao<DocGia> {
    
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
            System.err.println("Lỗi khi thêm độc giả: " + e.getMessage());
            e.printStackTrace();
            return 0; 
        }
    }

    public String getMaDocGiaByUsername(String username) {
        String sql = "SELECT maNguoiDung FROM DocGia WHERE taiKhoan = '" + username + "'";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String result = rs.getString("maNguoiDung");
                return result;
            }
            return "DG001";
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã người dùng : " + e.getMessage());
            e.printStackTrace();
            return "DG001";
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
            return "DG001";
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy mã người dùng mới nhất: " + e.getMessage());
            e.printStackTrace();
            return "DG001";
        }
    }
    
    @Override
    public int xoaDoiTuong(DocGia t) {
        String maNguoiDung = t.getMaNguoiDung();
        String sqlPhieuPhat = "DELETE FROM phieuphat WHERE maDocGia = ?";
        String sqlLichSu = "DELETE FROM lichsumuonsach WHERE maDocGia = ?";
        String sqlXoaDocGia = "DELETE FROM docgia WHERE maNguoiDung = ?";

        try (Connection conn = JDBCUtil.connect()) {
            conn.setAutoCommit(false);

            try (
                PreparedStatement stmt1 = conn.prepareStatement(sqlPhieuPhat);
                PreparedStatement stmt2 = conn.prepareStatement(sqlLichSu);
                PreparedStatement stmtDel = conn.prepareStatement(sqlXoaDocGia)
            ) {
                // Xóa bản ghi trong phieuphat
                stmt1.setString(1, maNguoiDung); // Dùng maNguoiDung thay vì maDocGia
                stmt1.executeUpdate();

                // Xóa bản ghi trong lichsumuonsach
                stmt2.setString(1, maNguoiDung);
                stmt2.executeUpdate();

                // Xóa bản ghi trong docgia
                stmtDel.setString(1, maNguoiDung);
                int result = stmtDel.executeUpdate();

                conn.commit();
                return result;
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Lỗi khi xóa độc giả: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
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
            stmt.setDate(6, new Date(t.getNgayTao().getTime()));
            stmt.setString(7, t.getMaNguoiDung());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
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
        return list;
    }

    public List<DocGia> timKiem(String keyword) {
        List<DocGia> list = new ArrayList<>();
        String sql = "SELECT * FROM DocGia WHERE maNguoiDung LIKE ? OR tenNguoiDung LIKE ? OR taiKhoan LIKE ? OR email LIKE ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) {
                stmt.setString(i, like);
            }
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

    public List<DocGia> layDanhSachTheoMa(String dk) {
        List<DocGia> list = new ArrayList<>();
        String sql = "SELECT * FROM DocGia WHERE maNguoiDung LIKE ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        return list;
    }

    @Override
    public List<DocGia> layDanhSachTheoDK(String dk) {
        return null; // TODO: Implement if needed
    }

    // Thêm phương thức kiểm tra tài khoản tồn tại
    public boolean kiemTraTaiKhoanTonTai(String taiKhoan) {
        String sql = "SELECT COUNT(*) FROM DocGia WHERE taiKhoan = ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, taiKhoan);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra tài khoản: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Thêm phương thức lấy tên người dùng theo mã người dùng
    public String getTenNguoiDungByMaNguoiDung(String maNguoiDung) {
        String sql = "SELECT tenNguoiDung FROM DocGia WHERE maNguoiDung = ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maNguoiDung);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tenNguoiDung");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tên người dùng: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}