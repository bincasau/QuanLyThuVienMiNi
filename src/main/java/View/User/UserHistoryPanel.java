package View.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import DAO.DocGiaDao;
import DAO.LichSuMuonSachDao;
import DAO.SachDao;
import Model.DocGia;
import Model.LichSuMuonSach;
import Model.Sach;

public class UserHistoryPanel extends JPanel {
    private String maDocGia;
    private JPanel pnl_ContentArea;
    private List<LichSuMuonSach> lichSuList;
    private JTextField txt_Search;
    private JButton btn_ClearSearch;
    private JComboBox<String> cmb_Filter;
    private Map<String, String> sachCache = new HashMap<>();

    public UserHistoryPanel(String maDocGia) {
        this.maDocGia = maDocGia;
        initializeData();
        initializeUI();
    }

    public UserHistoryPanel() {
        this.maDocGia = "DG001";
        initializeData();
        initializeUI();
    }

    private void initializeData() {
        if (maDocGia == null || maDocGia.trim().isEmpty()) {
            System.err.println("Mã độc giả không hợp lệ: " + maDocGia);
            lichSuList = List.of();
            return;
        }

        try {
            DocGiaDao docGiaDao = DocGiaDao.getInstance();
            List<DocGia> docGiaList = docGiaDao.layDanhSachTheoMa(maDocGia);
            if (docGiaList == null || docGiaList.isEmpty()) {
                System.err.println("Không tìm thấy độc giả với mã: " + maDocGia);
                lichSuList = List.of();
                return;
            }

            LichSuMuonSachDao lichSuDao = LichSuMuonSachDao.getInstance();
            lichSuList = lichSuDao.layDanhSachTheoDK(maDocGia);
            if (lichSuList == null) {
                System.err.println("Lịch sử mượn sách trả về null cho mã: " + maDocGia);
                lichSuList = List.of();
            }

            // Tiền xử lý thông tin sách
            SachDao sachDao = SachDao.getInstance();
            for (LichSuMuonSach lichSu : lichSuList) {
                String maSach = lichSu.getMaSach();
                if (!sachCache.containsKey(maSach)) {
                    List<Sach> sachList = sachDao.layDanhSachTheoDK(maSach);
                    String tenSach = sachList.isEmpty() ? "Không tìm thấy" : sachList.get(0).getTenSach();
                    sachCache.put(maSach, tenSach);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy dữ liệu: " + e.getMessage());
            e.printStackTrace();
            lichSuList = List.of();
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 600));

        JPanel pnl_Sidebar = createSidebar();
        add(pnl_Sidebar, BorderLayout.WEST);

        JPanel pnl_Main = new JPanel(new BorderLayout());

        JPanel pnl_Header = createHeader();
        pnl_Main.add(pnl_Header, BorderLayout.NORTH);

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
        btn_History.setEnabled(false);
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

        // Top row with avatar, user info, and notification
        JPanel pnl_TopRow = new JPanel();
        pnl_TopRow.setLayout(new BoxLayout(pnl_TopRow, BoxLayout.X_AXIS));
        pnl_TopRow.setOpaque(false);
        pnl_TopRow.setBorder(BorderFactory.createEmptyBorder(10, 35, 0, 20));

        JButton btn_Avatar = createIconButton("pictures/profile.png", "👤");
        JPanel pnl_UserInfo = new JPanel();
        pnl_UserInfo.setLayout(new BoxLayout(pnl_UserInfo, BoxLayout.Y_AXIS));
        pnl_UserInfo.setOpaque(false);

        String tenNguoiDung = "Không tìm thấy";
        if (maDocGia != null && !maDocGia.trim().isEmpty()) {
            DocGiaDao docGiaDao = DocGiaDao.getInstance();
            List<DocGia> docGiaList = docGiaDao.layDanhSachTheoMa(maDocGia);
            if (docGiaList != null && !docGiaList.isEmpty()) {
                tenNguoiDung = docGiaList.get(0).getTenNguoiDung();
            }
        }

        JLabel lbl_Name = new JLabel(tenNguoiDung);
        lbl_Name.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl_Name.setForeground(Color.WHITE);
        JLabel lbl_ID = new JLabel("Mã độc giả: " + maDocGia);
        lbl_ID.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl_ID.setForeground(Color.WHITE);

        JButton btn_Notification = createIconButton("pictures/bell.png", "🔔");

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

        // Search row with text field and combo box
        JPanel pnl_SearchRow = new JPanel();
        pnl_SearchRow.setLayout(new BoxLayout(pnl_SearchRow, BoxLayout.X_AXIS));
        pnl_SearchRow.setOpaque(false);
        pnl_SearchRow.setBorder(BorderFactory.createEmptyBorder(10, 35, 10, 0));

        // Search field
        txt_Search = new JTextField("Tìm kiếm mã sách, tên sách, ngày mượn, ngày trả...");
        txt_Search.setPreferredSize(new Dimension(300, 25));
        txt_Search.setMaximumSize(new Dimension(500, 25));
        txt_Search.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txt_Search.setForeground(Color.GRAY);

        // Placeholder handling
        txt_Search.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txt_Search.getText().equals("Tìm kiếm mã sách, tên sách, ngày mượn, ngày trả...")) {
                    txt_Search.setText("");
                    txt_Search.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txt_Search.getText().isEmpty()) {
                    txt_Search.setText("Tìm kiếm mã sách, tên sách, ngày mượn, ngày trả...");
                    txt_Search.setForeground(Color.GRAY);
                }
            }
        });

        // Auto-filter on text change
        txt_Search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (txt_Search.isEditable()) {
                    String filterValue = cmb_Filter.getSelectedItem().equals("Tất cả") ? null : (String) cmb_Filter.getSelectedItem();
                    updateHistoryContent(txt_Search.getText(), filterValue);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (txt_Search.isEditable()) {
                    String filterValue = cmb_Filter.getSelectedItem().equals("Tất cả") ? null : (String) cmb_Filter.getSelectedItem();
                    updateHistoryContent(txt_Search.getText(), filterValue);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (txt_Search.isEditable()) {
                    String filterValue = cmb_Filter.getSelectedItem().equals("Tất cả") ? null : (String) cmb_Filter.getSelectedItem();
                    updateHistoryContent(txt_Search.getText(), filterValue);
                }
            }
        });

        // Search icon
        ImageIcon searchIcon = new ImageIcon("pictures/search.png");
        Image scaledImage = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        searchIcon = new ImageIcon(scaledImage);

        RoundedButton btn_SearchIcon = new RoundedButton("🔍", 10);
        btn_SearchIcon.setIcon(searchIcon);
        btn_SearchIcon.setPreferredSize(new Dimension(25, 25));
        btn_SearchIcon.setMaximumSize(new Dimension(25, 25));
        btn_SearchIcon.setBackground(new Color(240, 240, 240));
        btn_SearchIcon.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btn_SearchIcon.addActionListener(e -> {
            if (txt_Search.isEditable()) {
                String filterValue = cmb_Filter.getSelectedItem().equals("Tất cả") ? null : (String) cmb_Filter.getSelectedItem();
                updateHistoryContent(txt_Search.getText(), filterValue);
            }
        });

        // Clear search button
        btn_ClearSearch = new RoundedButton("Xóa", 10);
        btn_ClearSearch.setPreferredSize(new Dimension(60, 25));
        btn_ClearSearch.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn_ClearSearch.setBackground(Color.LIGHT_GRAY);
        btn_ClearSearch.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btn_ClearSearch.setVisible(false);
        btn_ClearSearch.addActionListener(e -> {
            txt_Search.setText("Tìm kiếm mã sách, tên sách, ngày mượn, ngày trả...");
            txt_Search.setForeground(Color.GRAY);
            txt_Search.setEditable(true);
            btn_ClearSearch.setVisible(false);
            cmb_Filter.setSelectedItem("Tất cả");
            updateHistoryContent(null, null);
        });

        // Combo box for filtering
        String[] filterOptions = {"Tất cả", "Chưa trả", "Đã trả"};
        cmb_Filter = new JComboBox<>(filterOptions);
        cmb_Filter.setPreferredSize(new Dimension(120, 25));
        cmb_Filter.setMaximumSize(new Dimension(120, 25));
        cmb_Filter.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cmb_Filter.setBackground(Color.WHITE);
        cmb_Filter.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cmb_Filter.addActionListener(e -> {
            String selectedFilter = (String) cmb_Filter.getSelectedItem();
            String filterValue = selectedFilter.equals("Tất cả") ? null : selectedFilter;
            updateHistoryContent(txt_Search.getText(), filterValue);
        });

        pnl_SearchRow.add(txt_Search);
        pnl_SearchRow.add(Box.createHorizontalStrut(5));
        pnl_SearchRow.add(btn_SearchIcon);
        pnl_SearchRow.add(Box.createHorizontalStrut(5));
        pnl_SearchRow.add(btn_ClearSearch);
        pnl_SearchRow.add(Box.createHorizontalStrut(10));
        pnl_SearchRow.add(cmb_Filter);

        pnl_Header.add(pnl_TopRow, BorderLayout.NORTH);
        pnl_Header.add(pnl_SearchRow, BorderLayout.CENTER);

        return pnl_Header;
    }

    private JButton createIconButton(String imagePath, String fallbackText) {
        ImageIcon icon = new ImageIcon(imagePath);
        JButton button;
        if (icon.getIconWidth() != -1) {
            Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            button = new JButton(new ImageIcon(scaled));
        } else {
            button = new JButton(fallbackText);
        }
        button.setPreferredSize(new Dimension(60, 60));
        button.setMaximumSize(new Dimension(60, 60));
        button.setFocusable(false);
        button.setBackground(new Color(240, 240, 240));
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return button;
    }

    private JPanel createHistoryContent() {
        JPanel pnl_Content = new JPanel();
        pnl_Content.setLayout(new BorderLayout());
        pnl_Content.setBackground(Color.WHITE);
        pnl_Content.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));

        pnl_ContentArea = new JPanel();
        pnl_ContentArea.setLayout(new BoxLayout(pnl_ContentArea, BoxLayout.Y_AXIS));
        pnl_ContentArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(pnl_ContentArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        pnl_Content.add(scrollPane, BorderLayout.CENTER);

        updateHistoryContent(null, null);
        return pnl_Content;
    }

    private void updateHistoryContent(String searchText, String filter) {
        pnl_ContentArea.removeAll();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        boolean hasData = false;

        // Normalize search text
        String normalizedSearch = searchText == null || 
            searchText.trim().isEmpty() || 
            searchText.equals("Tìm kiếm mã sách, tên sách, ngày mượn, ngày trả...") 
            ? null : searchText.trim().toLowerCase();
        String normalizedSearchDate = normalizedSearch != null ? normalizedSearch.replace("/", "").replace("-", "") : null;

        // Duyệt danh sách lịch sử một lần duy nhất
        for (LichSuMuonSach lichSu : lichSuList) {
            String maSach = lichSu.getMaSach();
            String tenSach = sachCache.getOrDefault(maSach, "Không tìm thấy");
            String ngayMuon = (lichSu.getNgayMuon() != null) ? dateFormat.format(lichSu.getNgayMuon()) : "";
            String ngayTra = (lichSu.getNgayTra() != null) ? dateFormat.format(lichSu.getNgayTra()) : "";
            String trangThai = lichSu.getTrangThai();

            // Normalize date for flexible matching
            String normalizedNgayMuon = ngayMuon.replace("/", "");
            String normalizedNgayTra = ngayTra.replace("/", "");

            // Check if item matches search text
            boolean matchesSearch = normalizedSearch == null ||
                    maSach.toLowerCase().contains(normalizedSearch) ||
                    tenSach.toLowerCase().contains(normalizedSearch) ||
                    normalizedNgayMuon.contains(normalizedSearchDate) ||
                    normalizedNgayTra.contains(normalizedSearchDate);

            // Check if item matches status filter
            boolean matchesFilter = filter == null || trangThai.equals(filter);

            if (matchesSearch && matchesFilter) {
                hasData = true;
                String bookTitle = "Tên sách: " + tenSach + " (" + maSach + ")";
                boolean isBorrowing = "Chưa trả".equals(trangThai);
                JPanel itemPanel = createHistoryItem(bookTitle, 
                    "Mượn: " + (ngayMuon.isEmpty() ? "N/A" : ngayMuon) + 
                    ", Trả: " + (ngayTra.isEmpty() ? "N/A" : ngayTra), 
                    isBorrowing);
                pnl_ContentArea.add(itemPanel);
                pnl_ContentArea.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

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
        lbl_BookTitle.setPreferredSize(new Dimension(300, 25));

        JLabel lbl_Date = new JLabel(date);
        lbl_Date.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl_Date.setPreferredSize(new Dimension(250, 25));

        JLabel lbl_Status = new JLabel(isBorrowing ? "Chưa trả" : "Đã trả");
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
            JFrame frame = new JFrame("Thư viện mini");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.add(new UserHistoryPanel("DG002"));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}