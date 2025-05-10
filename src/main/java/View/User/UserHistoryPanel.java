package View.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import DAO.DocGiaDao;
import DAO.LichSuMuonSachDao;
import Model.DocGia;
import Model.LichSuMuonSach;

public class UserHistoryPanel extends JPanel {
    private String maDocGia;
    private JPanel pnl_ContentArea; // Để cập nhật nội dung khi lọc
    private List<LichSuMuonSach> lichSuList; // Dữ liệu từ cơ sở dữ liệu

    // Constructor nhận maDocGia
    public UserHistoryPanel(String maDocGia) {
        this.maDocGia = maDocGia;
        initializeData();
        initializeUI();
    }

    public UserHistoryPanel() {
        this.maDocGia = "DG001"; // Mặc định, thay bằng mã độc giả hợp lệ
        initializeData();
        initializeUI();
    }

    private void initializeData() {
        // Kiểm tra maDocGia có hợp lệ không
        if (maDocGia == null || maDocGia.trim().isEmpty()) {
            System.err.println("Mã độc giả không hợp lệ");
            lichSuList = List.of();
            return;
        }

        // Kiểm tra xem maDocGia có tồn tại trong cơ sở dữ liệu không
        DocGiaDao docGiaDao = DocGiaDao.getInstance();
        List<DocGia> docGiaList = docGiaDao.layDanhSachTheoMa(maDocGia);
        if (docGiaList == null || docGiaList.isEmpty()) {
            System.err.println("Không tìm thấy độc giả với mã: " + maDocGia);
            lichSuList = List.of();
            return;
        }

        // Lấy danh sách lịch sử mượn sách từ maDocGia
        LichSuMuonSachDao lichSuDao = LichSuMuonSachDao.getInstance();
        lichSuList = lichSuDao.layDanhSachTheoDK(maDocGia);
        if (lichSuList == null) {
            lichSuList = List.of(); // Đảm bảo không trả về null để tránh NullPointerException
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 600));

        // Sidebar
        JPanel pnl_Sidebar = createSidebar();
        add(pnl_Sidebar, BorderLayout.WEST);

        // Main panel
        JPanel pnl_Main = new JPanel(new BorderLayout());

        // Header
        JPanel pnl_Header = createHeader();
        pnl_Main.add(pnl_Header, BorderLayout.NORTH);

        // Content area with history list
        JPanel pnl_Content = createHistoryContent();
        pnl_Main.add(pnl_Content, BorderLayout.CENTER);

        add(pnl_Main, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel pnl_Sidebar = new JPanel();
        pnl_Sidebar.setLayout(new BoxLayout(pnl_Sidebar, BoxLayout.Y_AXIS));
        pnl_Sidebar.setPreferredSize(new Dimension(220, 600));
        pnl_Sidebar.setBackground(new Color(240, 233, 222));

        JLabel lbl_Title = new JLabel("Thư viện MINI");
        lbl_Title.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbl_Title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_Sidebar.add(lbl_Title);

        JPanel pnl_ButtonGroup = new JPanel();
        pnl_ButtonGroup.setLayout(new BoxLayout(pnl_ButtonGroup, BoxLayout.Y_AXIS));
        pnl_ButtonGroup.setBackground(new Color(240, 233, 222));
        pnl_ButtonGroup.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        RoundedButton btn_Home = new RoundedButton("Trang chủ", 20);
        btn_Home.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn_Home.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn_Home.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_Home.setHorizontalAlignment(SwingConstants.LEFT);
        pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        pnl_ButtonGroup.add(btn_Home);

        RoundedButton btn_History = new RoundedButton("Lịch sử", 20);
        btn_History.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn_History.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn_History.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_History.setHorizontalAlignment(SwingConstants.LEFT);
        btn_History.setEnabled(false); // Đánh dấu nút Lịch sử đang chọn
        pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        pnl_ButtonGroup.add(btn_History);

        RoundedButton btn_Penalty = new RoundedButton("Phiếu phạt", 20);
        btn_Penalty.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn_Penalty.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn_Penalty.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_Penalty.setHorizontalAlignment(SwingConstants.LEFT);
        pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        pnl_ButtonGroup.add(btn_Penalty);

        RoundedButton btn_Logout = new RoundedButton("Đăng xuất", 20);
        btn_Logout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn_Logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn_Logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_Logout.setHorizontalAlignment(SwingConstants.LEFT);

        btn_Logout.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(btn_Logout);
            int confirm = JOptionPane.showConfirmDialog(window,
                    "Bạn có chắc chắn muốn đăng xuất?",
                    "Xác nhận đăng xuất",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (window != null) {
                    window.dispose();
                }
                View.Login.Login loginForm = new View.Login.Login();
                loginForm.setVisible(true);
            }
        });

        pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        pnl_ButtonGroup.add(btn_Logout);

        pnl_Sidebar.add(pnl_ButtonGroup);
        return pnl_Sidebar;
    }

    private JPanel createHeader() {
        JPanel pnl_Header = new JPanel(new BorderLayout());
        pnl_Header.setPreferredSize(new Dimension(0, 160));
        pnl_Header.setBackground(new Color(107, 142, 35));

        JPanel pnl_TopRow = new JPanel();
        pnl_TopRow.setLayout(new BoxLayout(pnl_TopRow, BoxLayout.X_AXIS));
        pnl_TopRow.setOpaque(false);
        pnl_TopRow.setBorder(BorderFactory.createEmptyBorder(10, 35, 0, 20));

        JButton btn_Avatar = new JButton("AVT");
        JPanel pnl_UserInfo = new JPanel();
        pnl_UserInfo.setLayout(new BoxLayout(pnl_UserInfo, BoxLayout.Y_AXIS));
        pnl_UserInfo.setOpaque(false);

        // Mặc định hiển thị nếu không tìm thấy thông tin
        String tenNguoiDung = "Không tìm thấy";

        // Lấy thông tin độc giả từ maDocGia
        if (maDocGia != null && !maDocGia.trim().isEmpty()) {
            DocGiaDao docGiaDao = DocGiaDao.getInstance();
            List<DocGia> docGiaList = docGiaDao.layDanhSachTheoMa(maDocGia);
            if (docGiaList != null && !docGiaList.isEmpty()) {
                tenNguoiDung = docGiaList.get(0).getTenNguoiDung();
            }
        }

        JLabel lbl_Name = new JLabel(tenNguoiDung); // Hiển thị tên độc giả
        lbl_Name.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl_Name.setForeground(Color.WHITE);
        JLabel lbl_ID = new JLabel("Mã độc giả: " + maDocGia); // Hiển thị mã độc giả
        lbl_ID.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl_ID.setForeground(Color.WHITE);

        JButton btn_Notification = new JButton("Noti");

        btn_Avatar.setFont(new Font("SansSerif", Font.PLAIN, 10));
        btn_Avatar.setFocusable(false);
        btn_Avatar.setPreferredSize(new Dimension(60, 60));
        btn_Avatar.setMaximumSize(new Dimension(60, 60));

        btn_Notification.setFont(new Font("SansSerif", Font.PLAIN, 10));
        btn_Notification.setFocusable(false);
        btn_Notification.setPreferredSize(new Dimension(60, 60));
        btn_Notification.setMaximumSize(new Dimension(60, 60));

        btn_Avatar.setAlignmentY(Component.CENTER_ALIGNMENT);
        btn_Notification.setAlignmentY(Component.CENTER_ALIGNMENT);
        lbl_Name.setAlignmentY(Component.CENTER_ALIGNMENT);
        lbl_ID.setAlignmentY(Component.CENTER_ALIGNMENT);

        pnl_UserInfo.add(lbl_Name);
        pnl_UserInfo.add(lbl_ID);

        pnl_TopRow.add(btn_Avatar);
        pnl_TopRow.add(Box.createHorizontalStrut(10));
        pnl_TopRow.add(pnl_UserInfo);
        pnl_TopRow.add(Box.createHorizontalGlue());
        pnl_TopRow.add(btn_Notification);

        pnl_Header.add(pnl_TopRow, BorderLayout.NORTH);

        return pnl_Header;
    }

    private JPanel createHistoryContent() {
        JPanel pnl_Content = new JPanel();
        pnl_Content.setLayout(new BorderLayout());
        pnl_Content.setBackground(Color.WHITE);
        pnl_Content.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));

        // Tiêu đề và nút lọc trên cùng một hàng
        JPanel pnl_TitleRow = new JPanel();
        pnl_TitleRow.setLayout(new BoxLayout(pnl_TitleRow, BoxLayout.X_AXIS));
        pnl_TitleRow.setOpaque(false);
        pnl_TitleRow.setBorder(BorderFactory.createEmptyBorder(10, 35, 0, 0));
        pnl_TitleRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton btn_History = new RoundedButton("Lịch sử mượn sách", 10);
        btn_History.setPreferredSize(new Dimension(150, 25));
        btn_History.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn_History.setBackground(Color.LIGHT_GRAY);
        btn_History.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btn_History.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_History.addActionListener(e -> updateHistoryContent(null));

        RoundedButton btn_Borrowing = new RoundedButton("Đang mượn", 10);
        btn_Borrowing.setPreferredSize(new Dimension(100, 25));
        btn_Borrowing.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn_Borrowing.setBackground(Color.LIGHT_GRAY);
        btn_Borrowing.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btn_Borrowing.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_Borrowing.addActionListener(e -> updateHistoryContent("Chưa trả"));

        RoundedButton btn_Returned = new RoundedButton("Đã trả", 10);
        btn_Returned.setPreferredSize(new Dimension(80, 25));
        btn_Returned.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn_Returned.setBackground(Color.LIGHT_GRAY);
        btn_Returned.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btn_Returned.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_Returned.addActionListener(e -> updateHistoryContent("Đã trả"));

        pnl_TitleRow.add(btn_History);
        pnl_TitleRow.add(Box.createHorizontalStrut(5));
        pnl_TitleRow.add(btn_Borrowing);
        pnl_TitleRow.add(Box.createHorizontalStrut(5));
        pnl_TitleRow.add(btn_Returned);

        // Khu vực nội dung lịch sử
        pnl_ContentArea = new JPanel();
        pnl_ContentArea.setLayout(new BoxLayout(pnl_ContentArea, BoxLayout.Y_AXIS));
        pnl_ContentArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(pnl_ContentArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        // Thêm các thành phần vào panel chính
        pnl_Content.add(pnl_TitleRow, BorderLayout.NORTH);
        pnl_Content.add(scrollPane, BorderLayout.CENTER);

        // Khởi tạo nội dung ban đầu
        updateHistoryContent(null);
        return pnl_Content;
    }

    private void updateHistoryContent(String filter) {
        pnl_ContentArea.removeAll();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        boolean hasData = false;

        for (LichSuMuonSach lichSu : lichSuList) {
            String trangThai = lichSu.getTrangThai();
            if (filter == null || trangThai.equals(filter)) {
                hasData = true;
                String ngayMuon = (lichSu.getNgayMuon() != null) ? dateFormat.format(lichSu.getNgayMuon()) : "N/A";
                String ngayTra = (lichSu.getNgayTra() != null) ? dateFormat.format(lichSu.getNgayTra()) : "N/A";
                String bookTitle = "Tên sách: " + lichSu.getMaSach();
                boolean isBorrowing = "Chưa trả".equals(trangThai);
                JPanel itemPanel = createHistoryItem(bookTitle, "Mượn: " + ngayMuon + ", Trả: " + ngayTra, isBorrowing);
                pnl_ContentArea.add(itemPanel);
                pnl_ContentArea.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        // Nếu không có dữ liệu, hiển thị thông báo
        if (!hasData) {
            JLabel lbl_NoData = new JLabel("Không tìm thấy lịch sử");
            lbl_NoData.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lbl_NoData.setForeground(Color.GRAY);
            lbl_NoData.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnl_ContentArea.add(Box.createVerticalGlue());
            pnl_ContentArea.add(lbl_NoData);
            pnl_ContentArea.add(Box.createVerticalGlue());
        }

        pnl_ContentArea.revalidate();
        pnl_ContentArea.repaint();
    }

    private JPanel createHistoryItem(String bookTitle, String date, boolean isBorrowing) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
        Border roundedBorder = new LineBorder(Color.GRAY, 1, true) {
            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(5, 10, 5, 10);
            }

            @Override
            public void paintBorder(Component c, java.awt.Graphics g, int x, int y, int width, int height) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g.create();
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getLineColor());
                g2d.drawRoundRect(x, y, width - 1, height - 1, 20, 20);
                g2d.dispose();
            }
        };
        itemPanel.setBorder(roundedBorder);
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lbl_BookTitle = new JLabel(bookTitle);
        lbl_BookTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl_BookTitle.setPreferredSize(new Dimension(200, 25));

        JLabel lbl_Date = new JLabel(date);
        lbl_Date.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl_Date.setPreferredSize(new Dimension(250, 25));

        JLabel lbl_Status = new JLabel(isBorrowing ? "Đang mượn" : "Đã trả");
        lbl_Status.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl_Status.setForeground(isBorrowing ? Color.RED : Color.GREEN);

        itemPanel.add(lbl_BookTitle);
        itemPanel.add(Box.createHorizontalStrut(10));
        itemPanel.add(lbl_Date);
        itemPanel.add(Box.createHorizontalGlue());
        itemPanel.add(lbl_Status);

        return itemPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Thư viện mini - Lịch sử");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.add(new UserHistoryPanel("DG001")); // Thay DG001 bằng mã độc giả có trong cơ sở dữ liệu
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}