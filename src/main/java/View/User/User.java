package View.User;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import DAO.DocGiaDao;
import Model.DocGia;
import View.Login.Login;

public class User extends JPanel {
    private String username;
    private String maDocGia;
    private JPanel pnl_Cards;
    private PenaltyTicket penaltyPanel;
    private UserHistoryPanel historyPanel;
    private Dashboard dashboardPanel;
    private JTextField txt_Search;
    private CardLayout cardLayout;

    private static final String DASHBOARD_CARD = "Dashboard";
    private static final String HISTORY_CARD = "History";
    private static final String PENALTY_CARD = "Penalty";

    public User(String username) {
        this.username = username;
        this.maDocGia = fetchMaDocGia(username);
        initializeUI();
    }

    private String fetchMaDocGia(String username) {
        DocGiaDao docGiaDao = DocGiaDao.getInstance();
        for (DocGia docGia : docGiaDao.layDanhSach()) {
            if (docGia != null && docGia.getTenNguoiDung() != null && docGia.getTenNguoiDung().equals(username)) {
                return docGia.getMaNguoiDung();
            }
        }
        return "DG001"; // Fallback ID
    }

    private void initializeUI() {
        setLayout(new BorderLayout());  // Use panel layout
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel pnl_Sidebar = createSidebar();
        contentPane.add(pnl_Sidebar, BorderLayout.WEST);

        JPanel pnl_Main = new JPanel(new BorderLayout());

        cardLayout = new CardLayout();
        pnl_Cards = new JPanel(cardLayout);

        dashboardPanel = new Dashboard(username, this);
        pnl_Cards.add(dashboardPanel, DASHBOARD_CARD);

        historyPanel = new UserHistoryPanel(maDocGia);
        JScrollPane historyScrollPane = new JScrollPane(historyPanel);
        historyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        historyScrollPane.setBorder(null);
        pnl_Cards.add(historyScrollPane, HISTORY_CARD);

        penaltyPanel = new PenaltyTicket(username, maDocGia);
        pnl_Cards.add(penaltyPanel, PENALTY_CARD);

        cardLayout.show(pnl_Cards, DASHBOARD_CARD);

        pnl_Main.add(pnl_Cards, BorderLayout.CENTER);
        contentPane.add(pnl_Main, BorderLayout.CENTER);

        add(contentPane);  // Add to this panel
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

        String[] btnLabels = {"Trang chủ", "Lịch sử", "Phiếu phạt", "Đăng xuất"};
        for (String text : btnLabels) {
            JButton btn = new JButton(text);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            if (text.equals("Trang chủ")) {
                btn.addActionListener(e -> {
                    cardLayout.show(pnl_Cards, DASHBOARD_CARD);
                });
            } else if (text.equals("Lịch sử")) {
                btn.addActionListener(e -> {
                    cardLayout.show(pnl_Cards, HISTORY_CARD);
                });
            } else if (text.equals("Phiếu phạt")) {
                btn.addActionListener(e -> {
                    cardLayout.show(pnl_Cards, PENALTY_CARD);
                });
            } else if (text.equals("Đăng xuất")) {
                btn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Bạn có chắc chắn muốn đăng xuất?",
                            "Xác nhận đăng xuất",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null) {
                    window.dispose();
                }
                SwingUtilities.invokeLater(() -> {
                    new Login().setVisible(true);
                });
            }
                });
            }
            pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
            pnl_ButtonGroup.add(btn);
        }

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

        JButton btn_Avatar = createIconButton("pictures/profile.png", "👤");
        JButton btn_Notification = createIconButton("pictures/bell.png", "🔔");

        JLabel lbl_UserName = new JLabel(username);
        lbl_UserName.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl_UserName.setForeground(Color.WHITE);

        pnl_TopRow.add(btn_Avatar);
        pnl_TopRow.add(Box.createHorizontalStrut(10));
        pnl_TopRow.add(lbl_UserName);
        pnl_TopRow.add(Box.createHorizontalGlue());
        pnl_TopRow.add(btn_Notification);

        txt_Search = new JTextField("Nhập Tên Sách", 20);
        txt_Search.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txt_Search.setForeground(Color.GRAY);
        txt_Search.setPreferredSize(new Dimension(300, 40));

        txt_Search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txt_Search.getText().equals("Nhập Tên Sách")) {
                    txt_Search.setText("");
                    txt_Search.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txt_Search.getText().isEmpty()) {
                    txt_Search.setText("Nhập Tên Sách");
                    txt_Search.setForeground(Color.GRAY);
                }
            }
        });

        txt_Search.getDocument().addDocumentListener(new DocumentListener() {
            Timer timer = new Timer(300, null);

            {
                timer.setRepeats(false);
                timer.addActionListener(e -> {
                    String text = txt_Search.getText();
                    if (text.equals("Nhập Tên Sách")) {
                        text = "";
                    }
                    dashboardPanel.search(text);
                });
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                timer.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timer.restart();
            }
        });

        JButton btn_Clear = new JButton("✕");
        btn_Clear.setPreferredSize(new Dimension(40, 40));
        btn_Clear.addActionListener(e -> {
            txt_Search.setText("Nhập Tên Sách");
            txt_Search.setForeground(Color.GRAY);
            dashboardPanel.clearFilters();
        });

        JButton btn_Filter = new JButton("Lọc thể loại");
        btn_Filter.setPreferredSize(new Dimension(120, 40));
        btn_Filter.setFocusable(false);
        btn_Filter.addActionListener(e -> dashboardPanel.openFilterDialog());

        JButton btnClearFilter = new JButton("Hủy lọc");
        btnClearFilter.setPreferredSize(new Dimension(120, 40));
        btnClearFilter.setFocusable(false);
        btnClearFilter.addActionListener(e -> {
            txt_Search.setText("Nhập Tên Sách");
            txt_Search.setForeground(Color.GRAY);
            dashboardPanel.clearFilters();
        });

        JPanel pnl_SearchWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl_SearchWrapper.setOpaque(false);
        pnl_SearchWrapper.setBorder(BorderFactory.createEmptyBorder(15, 35, 10, 10));
        pnl_SearchWrapper.add(txt_Search);
        pnl_SearchWrapper.add(btn_Clear);
        pnl_SearchWrapper.add(btn_Filter);
        pnl_SearchWrapper.add(btnClearFilter);

        pnl_Header.add(pnl_TopRow, BorderLayout.NORTH);
        pnl_Header.add(pnl_SearchWrapper, BorderLayout.CENTER);

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
        return button;
    }
}