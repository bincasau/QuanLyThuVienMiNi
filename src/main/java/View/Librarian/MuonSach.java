package view.Librarian;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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

        JScrollPane scrollPane = new JScrollPane(pnl_MainContent);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panelMain.add(scrollPane, BorderLayout.CENTER);

        // Thêm sách mẫu
        for (int i = 0; i < 10; i++) {
            pnl_MainContent.add(createBookItem());
            pnl_MainContent.add(Box.createVerticalStrut(10));
        }

        return panelMain;
    }

    private JPanel createBookItem() {
        RoundedPanel itemPanel = new RoundedPanel(20);
        itemPanel.setLayout(new BorderLayout(10, 10));
        itemPanel.setBackground(new Color(182, 162, 162));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        ImageIcon bookIcon = new ImageIcon("Pictures/book.png");
        if (bookIcon.getIconWidth() != -1) {
            Image scaledBook = bookIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel lblBook = new JLabel(new ImageIcon(scaledBook));
            lblBook.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(lblBook);
        }

        JLabel lblBookName = new JLabel("Tên sách: JAVA 101");
        lblBookName.setFont(new Font("Arial", Font.BOLD, 12));
        lblBookName.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(lblBookName);

        itemPanel.add(leftPanel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        infoPanel.setOpaque(false);

        infoPanel.add(createInfoRow("Tên:", "Huỳnh Tuấn Phi"));
        infoPanel.add(createInfoRow("Mã KH:", "1111"));
        infoPanel.add(createInfoRow("Ngày mượn:", "1/1/111"));
        infoPanel.add(createInfoRow("Ngày trả:", "1/1/1111"));
        infoPanel.add(createStatusRow("Trạng thái:", "Chưa trả"));

        itemPanel.add(infoPanel, BorderLayout.CENTER);

        return itemPanel;
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lblLabel.setForeground(Color.BLACK);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 14));
        lblValue.setForeground(Color.BLACK);

        panel.add(lblLabel);
        panel.add(lblValue);
        return panel;
    }

    private JPanel createStatusRow(String label, String status) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lblLabel.setForeground(Color.BLACK);

        JLabel lblStatus = new JLabel(status);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 14));
        lblStatus.setForeground(Color.RED);

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
