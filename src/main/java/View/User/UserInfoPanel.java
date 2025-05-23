package View.User;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List; // Correct import for List
import DAO.DocGiaDao;
import Model.DocGia;

public class UserInfoPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private String maDocGia;

    public UserInfoPanel(String maDocGia) {
        this.maDocGia = maDocGia;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Fetch user information
        DocGia docGia = fetchUserInfo();
        
        // Header
        JLabel lblTitle = new JLabel("Thông tin cá nhân");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(new Color(107, 142, 35));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        if (docGia != null) {
            addInfoLabel(infoPanel, "Mã độc giả:", docGia.getMaNguoiDung(), 14, true);
            addInfoLabel(infoPanel, "Tên:", docGia.getTenNguoiDung(), 14, false);
            addInfoLabel(infoPanel, "Tài khoản:", docGia.getTaiKhoan(), 14, false);
            addInfoLabel(infoPanel, "Email:", docGia.getEmail() != null ? docGia.getEmail() : "Chưa cập nhật", 14, false);
            addInfoLabel(infoPanel, "Số điện thoại:", docGia.getSoDienThoai() != null ? docGia.getSoDienThoai() : "Chưa cập nhật", 14, false);
            addInfoLabel(infoPanel, "Ngày tạo:", docGia.getNgayTao() != null ? docGia.getNgayTao().toString() : "Chưa cập nhật", 14, false);
        } else {
            addInfoLabel(infoPanel, "Lỗi:", "Không tìm thấy thông tin người dùng", 14, true);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
    }

    private DocGia fetchUserInfo() {
        DocGiaDao docGiaDao = DocGiaDao.getInstance();
        List<DocGia> docGiaList = docGiaDao.layDanhSachTheoMa(maDocGia);
        return docGiaList.isEmpty() ? null : docGiaList.get(0);
    }

    private void addInfoLabel(JPanel panel, String title, String content, int fontSize, boolean bold) {
        JLabel lbl = new JLabel("<html><b>" + title + "</b> " + content + "</html>");
        lbl.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, fontSize));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(lbl);
    }
}