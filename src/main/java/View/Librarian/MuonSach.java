package View.Librarian;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import DAO.DocGiaDao;
import DAO.LichSuMuonSachDao;
import DAO.Ls_Dg_sachDao;
import DAO.SachDao;
import Model.DocGia;
import Model.LichSuMuonSach;
import Model.Ls_Dg_sach;
import Model.Sach;
import java.awt.*;
import java.util.List;

public class MuonSach extends JFrame {
    private JPanel pnl_Content;
    private JTextField txt_Search;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MuonSach frame = new MuonSach();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MuonSach() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setTitle("Thư viện mini");
        setLocationRelativeTo(null);

        pnl_Content = new JPanel(new BorderLayout());
        pnl_Content.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(pnl_Content);

        JPanel pnl_Sidebar = createSidebar();
        pnl_Content.add(pnl_Sidebar, BorderLayout.WEST);

        JPanel pnl_Header = createHeader();
        pnl_Content.add(pnl_Header, BorderLayout.NORTH);

        JPanel pnl_MainContent = createContent();
        pnl_Content.add(pnl_MainContent, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel pnl_Sidebar = new JPanel();
        pnl_Sidebar.setLayout(new BoxLayout(pnl_Sidebar, BoxLayout.Y_AXIS));
        pnl_Sidebar.setPreferredSize(new Dimension(200, 600));
        pnl_Sidebar.setBackground(new Color(106, 85, 85));

        String[] buttonNames = {
            "Sách", "Độc giả", "Mượn sách", "Thông tin phiếu phạt", "Thống kê", "Đăng xuất"
        };

        for (String name : buttonNames) {
            JButton btn = new JButton(name);
            btn.setPreferredSize(new Dimension(200, 50));
            btn.setMaximumSize(new Dimension(200, 50));
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            if (name.equals("Mượn sách")) {
                btn.setBackground(new Color(182, 162, 162));
                btn.setOpaque(true);
            }
            pnl_Sidebar.add(btn);
            if (name.equals("Thống kê")) {
                pnl_Sidebar.add(Box.createVerticalGlue());
            }
        }
        return pnl_Sidebar;
    }

    private JPanel createHeader() {
        JPanel pnl_Header = new JPanel();
        pnl_Header.setLayout(new BoxLayout(pnl_Header, BoxLayout.X_AXIS));
        pnl_Header.setPreferredSize(new Dimension(0, 100));
        pnl_Header.setBackground(new Color(106, 85, 85));

        // Logo
        ImageIcon icon_Book = new ImageIcon("pictures/book.png");
        if (icon_Book.getIconWidth() == -1) {
            JLabel lbl_icon = new JLabel("BOOK");
            lbl_icon.setFont(new Font("Arial", Font.BOLD, 20));
            lbl_icon.setForeground(Color.WHITE);
            lbl_icon.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 60));
            pnl_Header.add(lbl_icon);
        } else {
            Image scaledImage = icon_Book.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel lbl_icon = new JLabel(new ImageIcon(scaledImage));
            lbl_icon.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 60));
            pnl_Header.add(lbl_icon);
        }

        // Title
        JLabel lbl_Title = new JLabel("NHÀ SÁCH MINI");
        lbl_Title.setFont(new Font("Arial", Font.BOLD, 24));
        lbl_Title.setForeground(Color.WHITE);
        lbl_Title.setBorder(null);
        pnl_Header.add(lbl_Title);
        pnl_Header.add(Box.createHorizontalGlue());

        // Librarian name
        JLabel lbl_LibrarianName = new JLabel("Thủ thư: Nguyễn Văn A");
        lbl_LibrarianName.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl_LibrarianName.setForeground(Color.WHITE);
        lbl_LibrarianName.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        pnl_Header.add(lbl_LibrarianName);

        Dimension size = new Dimension(50, 50);

        // Notification icon
        ImageIcon icon_Bell = new ImageIcon("Pictures/bell.png");
        JButton btn_Notification;
        if (icon_Bell.getIconWidth() != -1) {
            Image scaledImage = icon_Bell.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            btn_Notification = new JButton(new ImageIcon(scaledImage));
        } else {
            btn_Notification = new JButton("🔔");
        }
        btn_Notification.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        btn_Notification.setPreferredSize(size);
        btn_Notification.setMaximumSize(size);
        btn_Notification.setMinimumSize(size);
        pnl_Header.add(btn_Notification);

        // Profile icon
        ImageIcon icon_Profile = new ImageIcon("Pictures/profile.png");
        JButton btn_Profile;
        if (icon_Profile.getIconWidth() != -1) {
            Image scaledImage = icon_Profile.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            btn_Profile = new JButton(new ImageIcon(scaledImage));
        } else {
            btn_Profile = new JButton("👤");
        }
        btn_Profile.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        btn_Profile.setPreferredSize(size);
        btn_Profile.setMaximumSize(size);
        btn_Profile.setMinimumSize(size);
        pnl_Header.add(btn_Profile);

        return pnl_Header;
    }

    private JPanel createContent() {
        JPanel panelMain = new JPanel(new BorderLayout());

        JPanel panelSearch = new JPanel(new BorderLayout(10, 10));
        panelSearch.setBackground(Color.WHITE);
        panelSearch.setBorder(new EmptyBorder(10, 10, 10, 10));

        txt_Search = new JTextField();
        panelSearch.add(txt_Search, BorderLayout.CENTER);

        JPanel panelButtonSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelButtonSearch.setBackground(Color.WHITE);

        JButton btn_Search = new JButton("🔍");
        JButton btn_Add = new JButton("➕");
        panelButtonSearch.add(btn_Search);
        panelButtonSearch.add(btn_Add);

        panelSearch.add(panelButtonSearch, BorderLayout.EAST);
        panelMain.add(panelSearch, BorderLayout.NORTH);

        JPanel pnl_MainContent = new JPanel();
        pnl_MainContent.setLayout(new BoxLayout(pnl_MainContent, BoxLayout.Y_AXIS));
        pnl_MainContent.setBackground(Color.WHITE);

        // Tạo JScrollPane với chiều cao động
        JScrollPane scrollPane = new JScrollPane(pnl_MainContent);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // Đảm bảo thanh cuộn luôn hiển thị

        // Điều chỉnh chiều cao cho JScrollPane sao cho phù hợp với không gian có sẵn
        scrollPane.setPreferredSize(new Dimension(900, 500));

        // Thêm các item vào pnl_MainConten
        List<Ls_Dg_sach> ds = Ls_Dg_sachDao.getInstance().layDanhSach();
        for (int i = 0; i < ds.size(); i++) {
        	Ls_Dg_sach ls = ds.get(i);
            pnl_MainContent.add(createBookItem(ls));
            pnl_MainContent.add(Box.createVerticalStrut(10));  // Tạo khoảng cách giữa các item
        }

        // Cập nhật lại chiều cao của scrollPane sau khi thêm tất cả các item vào pnl_MainContent
        pnl_MainContent.revalidate();
        pnl_MainContent.repaint();

        // Thêm JScrollPane vào panel chính
        panelMain.add(scrollPane, BorderLayout.CENTER);

        return panelMain;
    }

    private JPanel createBookItem(Ls_Dg_sach ls) {
        RoundedPanel itemPanel = new RoundedPanel(20);
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setBackground(new Color(182, 162, 162));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Giảm chiều cao để gọn
        itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.setBorder(new EmptyBorder(8, 15, 8, 15)); // Giảm padding tổng

        // LEFT: Ảnh và tên sách
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(150, 100));  // Tăng chiều rộng tại đây!

        ImageIcon bookIcon = new ImageIcon("Pictures/" + ls.getAnh()); // Đảm bảo tên hình ảnh đúng với tên sách
        if (bookIcon.getIconWidth() != -1) {
            Image scaledBook = bookIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel lblBook = new JLabel(new ImageIcon(scaledBook));
            lblBook.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(lblBook);
        }

        String bookName = "<html><div style='text-align: center; width: 120px;'>" + ls.getTenSach() + "</div></html>";
        JLabel lblBookName = new JLabel(bookName);
        lblBookName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBookName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBookName.setForeground(Color.BLACK);

        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(lblBookName);

        itemPanel.add(leftPanel, BorderLayout.WEST);

        // RIGHT: Các thông tin
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5); // Giảm khoảng cách dòng nhỏ lại
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        rightPanel.add(createInfoRow("Tên:", ls.getTenNguoiDung()), gbc); // Thêm tên người dùng
        gbc.gridy++;
        rightPanel.add(createInfoRow("Mã KH:", ls.getMaNguoiDung()), gbc); // Mã khách hàng
        gbc.gridy++;
        rightPanel.add(createInfoRow("Ngày mượn:", ls.getNgayMuon().toString()), gbc); // Ngày mượn
        gbc.gridy++;
        rightPanel.add(createInfoRow("Ngày trả:", ls.getNgayTra() == null ? "Chưa có" : ls.getNgayTra().toString()), gbc); // Ngày trả
        gbc.gridy++;
        rightPanel.add(createStatusRow("Trạng thái:", ls.getTrangThai()), gbc); // Trạng thái

        itemPanel.add(rightPanel, BorderLayout.CENTER);

        return itemPanel;
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        panel.setOpaque(false);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLabel.setForeground(Color.BLACK);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblValue.setForeground(Color.BLACK);

        panel.add(lblLabel);
        panel.add(lblValue);
        return panel;
    }

    private JPanel createStatusRow(String label, String status) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        panel.setOpaque(false);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLabel.setForeground(Color.BLACK);

        JLabel lblStatus = new JLabel(status);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblStatus.setForeground(status.equals("Chưa trả") ? Color.RED : Color.GREEN);

        panel.add(lblLabel);
        panel.add(lblStatus);
        return panel;
    }

    class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }
}
