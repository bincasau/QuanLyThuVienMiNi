package View.User;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import DAO.SachDao;
import Model.Sach;
import View.Login.Login;

public class Dashboard extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String fullName;
    private List<Sach> bookList;
    private List<Sach> fullBookList;
    private int currentBookIndex = 0;
    private static final int BATCH_SIZE = 20;
    private JPanel pnl_Content;
    private JTextField txt_Search;

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

        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            JScrollBar scrollBar = (JScrollBar) e.getSource();
            int extent = scrollBar.getModel().getExtent();
            int maximum = scrollBar.getModel().getMaximum();
            int value = scrollBar.getValue();
            if (value + extent >= maximum - 10 && currentBookIndex < bookList.size()) {
                loadMoreBooks();
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

        String[] btnLabels = {"Trang chủ", "Lịch sử", "Phiếu phạt", "Đăng xuất"};
        for (String text : btnLabels) {
            JButton btn = new JButton(text);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            if (text.equals("Đăng xuất")) {
                btn.addActionListener(e -> {
                    dispose();
                    SwingUtilities.invokeLater(() -> new Login().setVisible(true));
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

        JLabel lbl_UserName = new JLabel(fullName);
        lbl_UserName.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl_UserName.setForeground(Color.WHITE);

        pnl_TopRow.add(btn_Avatar);
        pnl_TopRow.add(Box.createHorizontalStrut(10));
        pnl_TopRow.add(lbl_UserName);
        pnl_TopRow.add(Box.createHorizontalGlue());
        pnl_TopRow.add(btn_Notification);

        txt_Search = new JTextField("Tìm kiếm...");
        txt_Search.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txt_Search.setForeground(Color.GRAY);

        txt_Search.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txt_Search.getText().equals("Tìm kiếm...")) {
                    txt_Search.setText("");
                    txt_Search.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (txt_Search.getText().isEmpty()) {
                    txt_Search.setText("Tìm kiếm...");
                    txt_Search.setForeground(Color.GRAY);
                }
            }
        });

        txt_Search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(txt_Search.getText()); }
            public void removeUpdate(DocumentEvent e) { search(txt_Search.getText()); }
            public void changedUpdate(DocumentEvent e) { search(txt_Search.getText()); }

            private void search(String keyword) {
                if (keyword.trim().isEmpty()) {
                    bookList = fullBookList;
                    currentBookIndex = 0;
                    pnl_Content.removeAll();
                    loadMoreBooks();
                } else {
                    updateBookListByKeyword(keyword);
                }
            }
        });

        JButton btn_Clear = new JButton("✕");
        btn_Clear.setPreferredSize(new Dimension(40, 40));
        btn_Clear.addActionListener(e -> {
            txt_Search.setText("");
            txt_Search.requestFocus();
            bookList = fullBookList;
            currentBookIndex = 0;
            pnl_Content.removeAll();
            loadMoreBooks();
        });

        JPanel pnl_SearchWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl_SearchWrapper.setOpaque(false);
        pnl_SearchWrapper.setBorder(BorderFactory.createEmptyBorder(15, 35, 10, 10));
        txt_Search.setPreferredSize(new Dimension(300, 40));
        pnl_SearchWrapper.add(txt_Search);
        pnl_SearchWrapper.add(btn_Clear);

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

    private JPanel createContentPanel() {
        pnl_Content = new JPanel();
        pnl_Content.setBackground(Color.WHITE);
        pnl_Content.setLayout(new BoxLayout(pnl_Content, BoxLayout.Y_AXIS));
        pnl_Content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        SachDao sachDao = SachDao.getInstance();
        fullBookList = sachDao.layDanhSach();
        bookList = fullBookList;

        loadMoreBooks();
        return pnl_Content;
    }

    private void loadMoreBooks() {
        int endIndex = Math.min(currentBookIndex + BATCH_SIZE, bookList.size());
        int count = 0;
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15)); // Adjusted gap
        rowPanel.setBackground(Color.WHITE);
        for (int i = currentBookIndex; i < endIndex; i++) {
            Sach book = bookList.get(i);
            JPanel card = createBookCard(book);
            rowPanel.add(card);
            count++;
            if (count % 4 == 0) {
                pnl_Content.add(rowPanel);
                rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
                rowPanel.setBackground(Color.WHITE);
            }
        }
        if (rowPanel.getComponentCount() > 0) pnl_Content.add(rowPanel);
        currentBookIndex = endIndex;
        pnl_Content.revalidate();
        pnl_Content.repaint();
    }

    private void updateBookListByKeyword(String keyword) {
        keyword = keyword.trim().toLowerCase();
        pnl_Content.removeAll();
        pnl_Content.setLayout(new BoxLayout(pnl_Content, BoxLayout.Y_AXIS));

        int columnsPerRow = 4;
        int count = 0;
        JPanel rowPanel = null;

        for (Sach book : fullBookList) {
            if (book.getTenSach().toLowerCase().contains(keyword)) {
                if (count % columnsPerRow == 0) {
                    rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
                    rowPanel.setBackground(Color.WHITE);
                    pnl_Content.add(rowPanel);
                }
                JPanel card = createBookCard(book);
                rowPanel.add(card);
                count++;
            }
        }

        pnl_Content.revalidate();
        pnl_Content.repaint();
    }

    private JPanel createBookCard(Sach book) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setPreferredSize(new Dimension(180, 260)); // Larger card

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon("pictures/" + book.getAnh());
            Image scaledImage = icon.getImage().getScaledInstance(140, 180, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            lblImage.setText("No Image");
        }
        lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel lblTitle = new JLabel(book.getTenSach());
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        lblTitle.setMaximumSize(new Dimension(160, 50));

        card.add(lblImage);
        card.add(Box.createVerticalStrut(5));
        card.add(lblTitle);

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