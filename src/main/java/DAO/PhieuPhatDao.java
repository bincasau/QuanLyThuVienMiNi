package DAO;

import Model.PhieuPhat;
import Util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhieuPhatDao implements InterfaceDao<PhieuPhat> {

    public static PhieuPhatDao getInstance() {
        return new PhieuPhatDao();
    }

    @Override
    public int themDoiTuong(PhieuPhat t) {
        String sql = "INSERT INTO phieuphat (maPhieuPhat, loi, giaTien, maDocGia, maSach) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getMaPhieuPhat());
            stmt.setString(2, t.getLoi());
            stmt.setDouble(3, t.getGiaTien());
            stmt.setString(4, t.getMaDocGia());
            stmt.setString(5, t.getMaSach());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int xoaDoiTuong(PhieuPhat t) {
        String sql = "DELETE FROM phieuphat WHERE maPhieuPhat = ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getMaPhieuPhat());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int capNhatDoiTuong(PhieuPhat t) {
        String sql = "UPDATE phieuphat SET loi = ?, giaTien = ?, maDocGia = ?, maSach = ? WHERE maPhieuPhat = ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getLoi());
            stmt.setDouble(2, t.getGiaTien());
            stmt.setString(3, t.getMaDocGia());
            stmt.setString(4, t.getMaSach());
            stmt.setString(5, t.getMaPhieuPhat());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<PhieuPhat> layDanhSach() {
        List<PhieuPhat> list = new ArrayList<>();
        String sql = "SELECT * FROM phieuphat";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                PhieuPhat pp = new PhieuPhat(
                    rs.getString("maPhieuPhat"),
                    rs.getString("loi"),
                    rs.getDouble("giaTien"),
                    rs.getString("maDocGia"),
                    rs.getString("maSach")
                );
                list.add(pp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<PhieuPhat> layDanhSachTheoDK(String dk) {
        List<PhieuPhat> list = new ArrayList<>();
        String sql = "SELECT * FROM phieuphat WHERE maDocGia = ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dk);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PhieuPhat pp = new PhieuPhat(
                        rs.getString("maPhieuPhat"),
                        rs.getString("loi"),
                        rs.getDouble("giaTien"),
                        rs.getString("maDocGia"),
                        rs.getString("maSach")
                    );
                    list.add(pp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public PhieuPhat getByMaPhieuPhat(String maPhieuPhat) {
        String sql = "SELECT * FROM phieuphat WHERE maPhieuPhat = ?";
        try (Connection conn = JDBCUtil.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhieuPhat);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new PhieuPhat(
                        rs.getString("maPhieuPhat"),
                        rs.getString("loi"),
                        rs.getDouble("giaTien"),
                        rs.getString("maDocGia"),
                        rs.getString("maSach")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateMaPhieuPhat() {
        String prefix = "PP";
        int startNumber = 1;
        String maPhieuPhat;

        try (Connection conn = JDBCUtil.connect()) {
            for (int i = startNumber; i <= 999; i++) {
                maPhieuPhat = String.format("%s%03d", prefix, i);
                String sql = "SELECT COUNT(*) FROM phieuphat WHERE maPhieuPhat = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, maPhieuPhat);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        return maPhieuPhat;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}