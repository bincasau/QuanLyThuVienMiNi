package View.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import DAO.SachDao;
import Model.Sach;
import Model.TheLoai;
import View.Login.Login;

public class Dashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String fullName;
    private List<Sach> bookList;
    private int currentBookIndex = 0;
    private static final int BATCH_SIZE = 20;
    private JPanel pnl_Content;

    public Dashboard(String fullName) {
        this.fullName = fullName;
        initializeUI();
    }

    public Dashboard() {
        this.fullName = "Guest";
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Thư viện mini");
        setSize(1000, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JPanel pnl_Sidebar = createSidebar();
        contentPane.add(pnl_Sidebar, BorderLayout.WEST);

        JPanel pnl_Main = new JPanel(new BorderLayout());

        JPanel pnl_Header = createHeader();
        pnl_Main.add(pnl_Header, BorderLayout.NORTH);

        pnl_Content = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(pnl_Content);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        // Add scroll listener for lazy loading
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                JScrollBar scrollBar = (JScrollBar) e.getSource();
                int extent = scrollBar.getModel().getExtent();
                int maximum = scrollBar.getModel().getMaximum();
                int value = scrollBar.getValue();
                // Check if user has scrolled to the bottom
                if (value + extent >= maximum - 10 && currentBookIndex < bookList.size()) {
                    loadMoreBooks();
                }
            }
        });

        pnl_Main.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(pnl_Main, BorderLayout.CENTER);
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

        JButton btn_Home = new JButton("Trang chủ");
        btn_Home.setBackground(new Color(252, 215, 194));
        btn_Home.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn_Home.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn_Home.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_Home.setHorizontalAlignment(SwingConstants.LEFT);
        pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        pnl_ButtonGroup.add(btn_Home);

        JButton btn_History = new JButton("Lịch sử");
        btn_History.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn_History.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn_History.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_History.setHorizontalAlignment(SwingConstants.LEFT);
        pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        pnl_ButtonGroup.add(btn_History);

        JButton btn_Penalty = new JButton("Phiếu phạt");
        btn_Penalty.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn_Penalty.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn_Penalty.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_Penalty.setHorizontalAlignment(SwingConstants.LEFT);
        pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
        pnl_ButtonGroup.add(btn_Penalty);

        JButton btn_Logout = new JButton("Đăng xuất");
        btn_Logout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn_Logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn_Logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn_Logout.setHorizontalAlignment(SwingConstants.LEFT);
        btn_Logout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                new Login().setVisible(true);
            });
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
        JLabel lbl_UserName = new JLabel(fullName);
        lbl_UserName.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl_UserName.setForeground(Color.WHITE);

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
        lbl_UserName.setAlignmentY(Component.CENTER_ALIGNMENT);

        pnl_TopRow.add(btn_Avatar);
        pnl_TopRow.add(Box.createHorizontalStrut(10));
        pnl_TopRow.add(lbl_UserName);
        pnl_TopRow.add(Box.createHorizontalGlue());
        pnl_TopRow.add(btn_Notification);

        JPanel pnl_SearchWithIcon = new JPanel();
        pnl_SearchWithIcon.setLayout(new BoxLayout(pnl_SearchWithIcon, BoxLayout.X_AXIS));
        pnl_SearchWithIcon.setOpaque(false);

        JTextField txt_Search = new JTextField("Tìm kiếm...");
        txt_Search.setPreferredSize(new Dimension(300, 40));
        txt_Search.setMaximumSize(new Dimension(500, 40));
        txt_Search.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txt_Search.setForeground(Color.GRAY);

        txt_Search.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txt_Search.getText().equals("Tìm kiếm...")) {
                    txt_Search.setText("");
                    txt_Search.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txt_Search.getText().isEmpty()) {
                    txt_Search.setText("Tìm kiếm...");
                    txt_Search.setForeground(Color.GRAY);
                }
            }
        });

        JButton btn_SearchIcon = new JButton("🔍");
        btn_SearchIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btn_SearchIcon.setFocusPainted(false);
        btn_SearchIcon.setContentAreaFilled(true);
        btn_SearchIcon.setBorderPainted(true);
        btn_SearchIcon.setPreferredSize(new Dimension(40, 40));
        btn_SearchIcon.setMaximumSize(new Dimension(40, 40));
        btn_SearchIcon.setMinimumSize(new Dimension(40, 40));
        btn_SearchIcon.setBackground(new Color(240, 240, 240));
        btn_SearchIcon.setOpaque(true);
        btn_SearchIcon.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btn_SearchIcon.setMargin(new Insets(0, 0, 0, 0));
        btn_SearchIcon.setHorizontalAlignment(SwingConstants.CENTER);
        btn_SearchIcon.setVerticalAlignment(SwingConstants.CENTER);

        JButton btn_FilterByCategory = new JButton("Tìm theo thể loại");
        btn_FilterByCategory.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn_FilterByCategory.setBackground(new Color(240, 240, 240));
        btn_FilterByCategory.setOpaque(true);
        btn_FilterByCategory.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btn_FilterByCategory.setPreferredSize(new Dimension(120, 40));
        btn_FilterByCategory.setMaximumSize(new Dimension(120, 40));

        pnl_SearchWithIcon.add(txt_Search);
        pnl_SearchWithIcon.add(Box.createRigidArea(new Dimension(5, 0)));
        pnl_SearchWithIcon.add(btn_SearchIcon);
        pnl_SearchWithIcon.add(Box.createRigidArea(new Dimension(10, 0)));
        pnl_SearchWithIcon.add(btn_FilterByCategory);

        JPanel pnl_SearchWrapper = new JPanel();
        pnl_SearchWrapper.setOpaque(false);
        pnl_SearchWrapper.setLayout(new BoxLayout(pnl_SearchWrapper, BoxLayout.X_AXIS));
        pnl_SearchWrapper.setBorder(BorderFactory.createEmptyBorder(15, 35, 10, 10));
        pnl_SearchWrapper.add(pnl_SearchWithIcon);
        pnl_SearchWrapper.add(Box.createHorizontalGlue());

        pnl_Header.add(pnl_TopRow, BorderLayout.NORTH);
        pnl_Header.add(pnl_SearchWrapper, BorderLayout.CENTER);

        return pnl_Header;
    }

    private JPanel createContentPanel() {
        pnl_Content = new JPanel();
        pnl_Content.setBackground(Color.WHITE);
        pnl_Content.setLayout(new GridLayout(0, 4, 10, 10));
        pnl_Content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        SachDao sachDao = SachDao.getInstance();
        bookList = sachDao.layDanhSach();

        // Load initial batch of books
        loadMoreBooks();

        return pnl_Content;
    }

    private void loadMoreBooks() {
        int endIndex = Math.min(currentBookIndex + BATCH_SIZE, bookList.size());
        for (int i = currentBookIndex; i < endIndex; i++) {
            Sach book = bookList.get(i);
            JPanel card = createBookCard(book);
            pnl_Content.add(card);
        }
        currentBookIndex = endIndex;

        // Update the layout and revalidate
        pnl_Content.revalidate();
        pnl_Content.repaint();
    }

    private JPanel createBookCard(Sach book) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setPreferredSize(new Dimension(220, 150));

        JLabel lblImage = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("pictures/" + book.getAnh());
            Image scaledImage = icon.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            lblImage.setText("No Image");
            lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        }
        lblImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        card.add(lblImage, BorderLayout.WEST);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        JLabel lblTitle = new JLabel(book.getTenSach());
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(lblTitle);

        JLabel lblPublisher = new JLabel("NXB: " + book.getnXB());
        lblPublisher.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPublisher.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(lblPublisher);

        JLabel lblYear = new JLabel("Năm: " + book.getNamXB());
        lblYear.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblYear.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(lblYear);

        StringBuilder genres = new StringBuilder("Thể loại: ");
        List<TheLoai> genreList = book.getDsTheLoai();
        if (genreList != null && !genreList.isEmpty()) {
            for (int i = 0; i < genreList.size(); i++) {
                genres.append(genreList.get(i).getTenTheLoai());
                if (i < genreList.size() - 1) {
                    genres.append(", ");
                }
            }
        } else {
            genres.append("N/A");
        }
        String genresText = "<html><body style='width: 150px'>" + genres.toString() + "</body></html>";
        JLabel lblGenres = new JLabel(genresText);
        lblGenres.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblGenres.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(lblGenres);

        card.add(detailsPanel, BorderLayout.CENTER);

        return card;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Dashboard frame = new Dashboard();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}