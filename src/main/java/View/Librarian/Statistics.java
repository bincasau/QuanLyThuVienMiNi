package View.Librarian;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controller.PdfController;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.Year;
import java.util.List;
import Model.ThongTinThongKe;

public class Statistics extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel pnl_content;
    private String username;
    private JPanel pnl_cards;
    private DecimalFormat currencyFormat;
    private PdfController pdfExportController = new PdfController();

    public Statistics(String username) {
        this.username = username;
        initializeUI();
    }

    public Statistics() {
        this.username = "Guest";
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setTitle("Thư viện mini");
        setLocationRelativeTo(null);

        // Initialize DecimalFormat for currency formatting
        currencyFormat = new DecimalFormat("#,##0.00");
        currencyFormat.setGroupingSize(3);

        pnl_content = new JPanel(new BorderLayout());
        pnl_content.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(pnl_content);

        JPanel pnl_Sidebar = createSidebar();
        pnl_content.add(pnl_Sidebar, BorderLayout.WEST);

        JPanel pnl_Main = createMain();
        pnl_Main.setBackground(Color.WHITE);
        pnl_content.add(pnl_Main, BorderLayout.CENTER);

        JPanel pnl_Header = createHeader();
        pnl_content.add(pnl_Header, BorderLayout.NORTH);
    }

    private JPanel createSidebar() {
        JPanel pnl_Sidebar = new JPanel();
        pnl_Sidebar.setLayout(new BoxLayout(pnl_Sidebar, BoxLayout.Y_AXIS));
        pnl_Sidebar.setPreferredSize(new Dimension(200, 600));
        pnl_Sidebar.setBackground(new Color(106, 85, 85));

        JButton btn_Book = new JButton("Sách");
        btn_Book.setPreferredSize(new Dimension(200, 50));
        btn_Book.setMaximumSize(new Dimension(200, 50));
        btn_Book.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn_Book.setOpaque(true);
        pnl_Sidebar.add(btn_Book);

        JButton btn_User = new JButton("Độc giả");
        btn_User.setPreferredSize(new Dimension(200, 50));
        btn_User.setMaximumSize(new Dimension(200, 50));
        btn_User.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_Sidebar.add(btn_User);

        JButton btn_Borrow = new JButton("Mượn sách");
        btn_Borrow.setPreferredSize(new Dimension(200, 50));
        btn_Borrow.setMaximumSize(new Dimension(200, 50));
        btn_Borrow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_Sidebar.add(btn_Borrow);

        JButton btn_PenaltyTicketInfo = new JButton("Thông tin phiếu phạt");
        btn_PenaltyTicketInfo.setPreferredSize(new Dimension(200, 50));
        btn_PenaltyTicketInfo.setMaximumSize(new Dimension(200, 50));
        btn_PenaltyTicketInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_Sidebar.add(btn_PenaltyTicketInfo);

        JButton btn_Statistics = new JButton("Thống kê");
        btn_Statistics.setPreferredSize(new Dimension(200, 50));
        btn_Statistics.setMaximumSize(new Dimension(200, 50));
        btn_Statistics.setBackground(new Color(182, 162, 162));
        btn_Statistics.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_Sidebar.add(btn_Statistics);

        pnl_Sidebar.add(Box.createVerticalGlue());

        JButton btn_Logout = new JButton("Đăng xuất");
        btn_Logout.setPreferredSize(new Dimension(200, 50));
        btn_Logout.setMaximumSize(new Dimension(200, 50));
        btn_Logout.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_Sidebar.add(btn_Logout);

        btn_Logout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                View.Login.Login loginForm = new View.Login.Login();
                loginForm.setVisible(true);
            });
        });

        return pnl_Sidebar;
    }

    private JPanel createHeader() {
        JPanel pnl_Header = new JPanel();
        pnl_Header.setLayout(new BoxLayout(pnl_Header, BoxLayout.X_AXIS));
        pnl_Header.setPreferredSize(new Dimension(0, 100));
        pnl_Header.setBackground(new Color(106, 85, 85));

        ImageIcon icon_Book = new ImageIcon("Pictures/book.png");
        if (icon_Book.getIconWidth() == -1) {
            JLabel lbl_icon = new JLabel("BOOK");
            lbl_icon.setFont(new Font("Arial", Font.BOLD, 20));
            lbl_icon.setForeground(Color.WHITE);
            lbl_icon.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 60));
            pnl_Header.add(lbl_icon);
        } else {
            Image scaledImage = icon_Book.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            icon_Book = new ImageIcon(scaledImage);
            JLabel lbl_icon = new JLabel(icon_Book);
            lbl_icon.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 60));
            pnl_Header.add(lbl_icon);
        }

        JLabel lbl_Title = new JLabel("NHÀ SÁCH MINI");
        lbl_Title.setFont(new Font("Arial", Font.BOLD, 24));
        lbl_Title.setForeground(Color.WHITE);
        lbl_Title.setBorder(null);
        pnl_Header.add(lbl_Title);
        pnl_Header.add(Box.createHorizontalGlue());

        JLabel lbl_LibrarianName = new JLabel("Thủ thư: " + username);
        lbl_LibrarianName.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl_LibrarianName.setForeground(Color.WHITE);
        lbl_LibrarianName.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        pnl_Header.add(lbl_LibrarianName);

        Dimension size = new Dimension(50, 50);

        ImageIcon icon_Bell = new ImageIcon("Pictures/bell.png");
        JButton btn_Notification;
        if (icon_Bell.getIconWidth() != -1) {
            Image scaledImage = icon_Bell.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            icon_Bell = new ImageIcon(scaledImage);
            btn_Notification = new JButton(icon_Bell);
        } else {
            btn_Notification = new JButton("🔔");
        }
        btn_Notification.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        btn_Notification.setPreferredSize(size);
        btn_Notification.setMaximumSize(size);
        btn_Notification.setMinimumSize(size);
        pnl_Header.add(btn_Notification);

        ImageIcon icon_Profile = new ImageIcon("Pictures/profile.png");
        JButton btn_Profile;
        if (icon_Profile.getIconWidth() != -1) {
            Image scaledImage = icon_Profile.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            icon_Profile = new ImageIcon(scaledImage);
            btn_Profile = new JButton(icon_Profile);
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

    private JPanel createMain() {
        JPanel pnl_Main = new JPanel(new BorderLayout());

        JPanel pnl_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl_top.setBackground(Color.WHITE);

        int currentYear = Year.now().getValue();
        Integer[] years = new Integer[currentYear - 2023 + 1];
        for (int i = 0; i < years.length; i++) {
            years[i] = 2023 + i;
        }
        JComboBox<Integer> yearComboBox = new JComboBox<>(years);
        yearComboBox.setPreferredSize(new Dimension(100, 40));
        yearComboBox.setMaximumSize(new Dimension(100, 40));
        yearComboBox.setSelectedItem(currentYear);
        pnl_top.add(yearComboBox);

        JButton btn_ExportPDF = new JButton("Xuất PDF");
        btn_ExportPDF.setPreferredSize(new Dimension(100, 40));
        btn_ExportPDF.setMaximumSize(new Dimension(100, 40));
        btn_ExportPDF.addActionListener(e -> {
            pdfExportController.exportToPDF((Integer) yearComboBox.getSelectedItem());
        });
        pnl_top.add(btn_ExportPDF);

        pnl_cards = new JPanel();
        pnl_cards.setLayout(new BoxLayout(pnl_cards, BoxLayout.Y_AXIS));
        pnl_cards.setBackground(Color.WHITE);
        pnl_cards.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(pnl_cards);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        updateStatisticsPanel(currentYear);

        yearComboBox.addActionListener(e -> {
            Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
            updateStatisticsPanel(selectedYear);
        });

        pnl_Main.add(pnl_top, BorderLayout.NORTH);
        pnl_Main.add(scrollPane, BorderLayout.CENTER);

        return pnl_Main;
    }

    private void updateStatisticsPanel(int year) {
        pnl_cards.removeAll();

        List<ThongTinThongKe> ds = DAO.ThongTinThongKeDao.getInstance().layDanhSachTheoNam(year);

        for (ThongTinThongKe thongKe : ds) {
            JPanel card = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w = getWidth();
                    int h = getHeight();
                    g2d.setColor(new Color(106, 85, 85));
                    g2d.fillRoundRect(0, 0, w, h, 20, 20);
                }
            };
            card.setLayout(new BorderLayout());
            card.setOpaque(false);
            card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            card.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Header with Month/Year
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            headerPanel.setOpaque(false);
            JLabel lblThangNam = new JLabel(String.format("%02d/%d", thongKe.getThang(), year));
            lblThangNam.setFont(new Font("Arial", Font.BOLD, 14));
            lblThangNam.setForeground(Color.BLACK);
            lblThangNam.setBackground(Color.WHITE);
            lblThangNam.setOpaque(true);
            lblThangNam.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            headerPanel.add(lblThangNam);
            card.add(headerPanel, BorderLayout.NORTH);

            // Statistics Panel
            JPanel statsPanel = new JPanel(new GridBagLayout());
            statsPanel.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.anchor = GridBagConstraints.WEST;

            // New Readers
            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel lblDocGiaMoi = new JLabel("Số lượng khách hàng đăng ký mới:");
            lblDocGiaMoi.setFont(new Font("Arial", Font.PLAIN, 14));
            lblDocGiaMoi.setForeground(Color.WHITE);
            statsPanel.add(lblDocGiaMoi, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JLabel valDocGiaMoi = new JLabel(String.valueOf(thongKe.getSoDocGiaMoi()));
            valDocGiaMoi.setFont(new Font("Arial", Font.BOLD, 14));
            valDocGiaMoi.setForeground(Color.WHITE);
            statsPanel.add(valDocGiaMoi, gbc);

            // Late Returns
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel lblTraSachTre = new JLabel("Số lượng sách quá hạn trả:");
            lblTraSachTre.setFont(new Font("Arial", Font.PLAIN, 14));
            lblTraSachTre.setForeground(Color.WHITE);
            statsPanel.add(lblTraSachTre, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JLabel valTraSachTre = new JLabel(String.valueOf(thongKe.getSoLuongTraSachTre()));
            valTraSachTre.setFont(new Font("Arial", Font.BOLD, 14));
            valTraSachTre.setForeground(Color.WHITE);
            statsPanel.add(valTraSachTre, gbc);

            // Lost Books
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel lblMatSach = new JLabel("Số lượng sách mất:");
            lblMatSach.setFont(new Font("Arial", Font.PLAIN, 14));
            lblMatSach.setForeground(Color.WHITE);
            statsPanel.add(lblMatSach, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JLabel valMatSach = new JLabel(String.valueOf(thongKe.getSoLuongMatSach()));
            valMatSach.setFont(new Font("Arial", Font.BOLD, 14));
            valMatSach.setForeground(Color.WHITE);
            statsPanel.add(valMatSach, gbc);

            // Damaged Books
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel lblHuSach = new JLabel("Số lượng sách hư hỏng:");
            lblHuSach.setFont(new Font("Arial", Font.PLAIN, 14));
            lblHuSach.setForeground(Color.WHITE);
            statsPanel.add(lblHuSach, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JLabel valHuSach = new JLabel(String.valueOf(thongKe.getSoLuongLamHuSach()));
            valHuSach.setFont(new Font("Arial", Font.BOLD, 14));
            valHuSach.setForeground(Color.WHITE);
            statsPanel.add(valHuSach, gbc);

            // Revenue
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel lblDoanhThu = new JLabel("Tổng thu:");
            lblDoanhThu.setFont(new Font("Arial", Font.PLAIN, 14));
            lblDoanhThu.setForeground(Color.WHITE);
            statsPanel.add(lblDoanhThu, gbc);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JLabel valDoanhThu = new JLabel(currencyFormat.format(thongKe.getDoanhThu()) + " VND");
            valDoanhThu.setFont(new Font("Arial", Font.BOLD, 14));
            valDoanhThu.setForeground(Color.WHITE);
            statsPanel.add(valDoanhThu, gbc);

            card.add(statsPanel, BorderLayout.CENTER);

            pnl_cards.add(card);
            pnl_cards.add(Box.createVerticalStrut(15));
        }

        pnl_cards.revalidate();
        pnl_cards.repaint();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Statistics frame = new Statistics();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}