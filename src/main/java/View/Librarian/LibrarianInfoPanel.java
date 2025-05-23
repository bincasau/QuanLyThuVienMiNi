package View.Librarian;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List; // Correct import for List
import DAO.ThuThuDao;
import Model.ThuThu;

public class LibrarianInfoPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private String maThuThu;

    public LibrarianInfoPanel(String maThuThu) {
        this.maThuThu = maThuThu;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Fetch Librarian information
        ThuThu librarian = fetchLibrarianInfo();
        
        // Header
        JLabel lblTitle = new JLabel("Thông tin cá nhân");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(new Color(139, 69, 69));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        if (librarian != null) {
            addInfoLabel(infoPanel, "Mã thủ thư:", librarian.getMaNguoiDung(), 14, true);
            addInfoLabel(infoPanel, "Tên:", librarian.getTenNguoiDung(), 14, false);
            addInfoLabel(infoPanel, "Tài khoản:", librarian.getTaiKhoan(), 14, false);
            addInfoLabel(infoPanel, "Email:", librarian.getEmail(), 14, false);
            addInfoLabel(infoPanel, "Số điện thoại:", librarian.getSoDienThoai(), 14, false);
        } else {
            addInfoLabel(infoPanel, "Lỗi:", "Không tìm thấy thông tin thủ thư", 14, true);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
    }

    private ThuThu fetchLibrarianInfo() {
        ThuThuDao thuThuDao = ThuThuDao.getInstance();
        // Use layDanhSachTheoDK with maThuThu as the condition (assuming maNguoiDung is queried)
        List<ThuThu> thuThuList = thuThuDao.layDanhSachTheoMaThuThu(maThuThu);
        return thuThuList.isEmpty() ? null : thuThuList.get(0);
    }

    private void addInfoLabel(JPanel panel, String title, String content, int fontSize, boolean bold) {
        JLabel lbl = new JLabel("<html><b>" + title + "</b> " + content + "</html>");
        lbl.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, fontSize));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(lbl);
    }
}