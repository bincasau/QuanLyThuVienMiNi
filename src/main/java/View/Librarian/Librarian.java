package View.Librarian;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import Controller.PdfController;
import DAO.DocGiaDao;
import DAO.LichSuMuonSachDao;
import DAO.Ls_Dg_sachDao;
import DAO.SachDao;
import DAO.TheLoaiDao;
import DAO.ThuThuDao;
import Model.DocGia;
import Model.LichSuMuonSach;
import Model.Ls_Dg_sach;
import Model.Sach;
import Model.TheLoai;
import Model.ThongTinThongKe;
import Model.ThuThu;
import Session.LoginSession;
import View.Login.Login;

public class Librarian extends JFrame {
    private String fullName;
    private JPanel mainPanel;
    private boolean isFiltering = false;
    private List<Sach> fullBookList;
    private List<Sach> displayedBooks;
    private JPanel pnl_center;
    private static final int BOOKS_PER_LOAD = 20;
    private DefaultTableModel tableModel;
    private JTable table;
    private int currentPage = 1;
    private int itemsPerPage = 10;
    private List<Ls_Dg_sach> filteredDs;
    private JPanel pnl_cards;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private Font itemFont = new Font("Segoe UI", Font.PLAIN, 12);
    private Font itemBoldFont = new Font("Segoe UI", Font.BOLD, 12);
    private PdfController pdfExportController = new PdfController();
    private DecimalFormat currencyFormat;
    private JPanel pnl_MainContent;
    private JScrollPane scrollPane;
    private JButton btn_prev;
    private JButton btn_next;

    public Librarian(String fullName) {
        this.fullName = fullName;
        this.mainPanel = createMainPanel();
    }

    public Librarian() {
        this.fullName = "Admin";
        this.mainPanel = createMainPanel();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel pnl_Sidebar = createSidebar();
        mainPanel.add(pnl_Sidebar, BorderLayout.WEST);

        tabbedPane.setBackground(Color.WHITE);
        // Ẩn thanh tab phía trên
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                return 0;
            }
            @Override
            protected int calculateTabAreaWidth(int tabPlacement, int vertRunCount, int maxTabWidth) {
                return 0;
            }
        });

        // Add tabs
        tabbedPane.addTab("Sách", createBookPanel());
        tabbedPane.addTab("Độc giả", createUserPanel());
        tabbedPane.addTab("Mượn sách", createBorrowPanel());
        tabbedPane.addTab("Thông tin phiếu phạt", createPenaltyTicketInfoPanel());
        tabbedPane.addTab("Thống kê", createStatisticsPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel pnl_Header = createHeader();
        mainPanel.add(pnl_Header, BorderLayout.NORTH);

        return mainPanel;
    }

    private JPanel createSidebar() {
        JPanel pnl_Sidebar = new JPanel();
        pnl_Sidebar.setLayout(new BoxLayout(pnl_Sidebar, BoxLayout.Y_AXIS));
        pnl_Sidebar.setPreferredSize(new Dimension(200, 600));
        pnl_Sidebar.setBackground(new Color(106, 85, 85));
    
        JButton[] buttons = new JButton[] {
            new JButton("Sách"),
            new JButton("Độc giả"),
            new JButton("Mượn sách"),
            new JButton("Thông tin phiếu phạt"),
            new JButton("Thống kê"),
            new JButton("Đăng xuất")
        };
    
        for (int i = 0; i < buttons.length; i++) {
            JButton btn = buttons[i];
            btn.setPreferredSize(new Dimension(200, 50));
            btn.setMaximumSize(new Dimension(200, 50));
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            btn.setOpaque(true);
            if (i < 5) {
                final int tabIndex = i;
                btn.addActionListener(e -> tabbedPane.setSelectedIndex(tabIndex));
            }
            if (i == 0) { // Highlight "Sách" by default
                btn.setBackground(new Color(182, 162, 162));
            }
            pnl_Sidebar.add(btn);
            if (i == 4) {
                pnl_Sidebar.add(Box.createVerticalGlue());
            }
        }
    
        // Xử lý sự kiện đăng xuất
        buttons[5].addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);
    
            if (confirm == JOptionPane.YES_OPTION) {
                LoginSession.getInstance().logout();
                // Close the current frame
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(pnl_Sidebar);
                if (frame != null) {
                    frame.setVisible(false);
                    frame.dispose();
                }
                // Open the login window
                SwingUtilities.invokeLater(() -> {
                    Login loginForm = new Login();
                    loginForm.setVisible(true);
                });
            }
        });
    
        // Update button highlighting when tabs change
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            for (int i = 0; i < 5; i++) {
                buttons[i].setBackground(i == selectedIndex ? new Color(182, 162, 162) : null);
            }
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

        JLabel lbl_LibrarianName = new JLabel("Thủ thư: " + fullName);
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
        btn_Profile.setMinimumSize(size);
        btn_Profile.setMaximumSize(size);
        pnl_Header.add(btn_Profile);

        return pnl_Header;
    }

    // Book Panel
    private JPanel createBookPanel() {
        JPanel pnl_Main = new JPanel(new BorderLayout());
        isFiltering = false;

        JPanel pnl_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl_top.setBackground(Color.WHITE);
        JTextField txt_search = new JTextField(25);
        txt_search.setPreferredSize(new Dimension(0, 40));
        txt_search.setMaximumSize(new Dimension(300, 40));
        JPopupMenu suggestionsPopup = new JPopupMenu();
        pnl_top.add(txt_search);
        pnl_top.add(suggestionsPopup);

        JList<String> suggestionList = new JList<>();
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionsPopup.setFocusable(false);
        suggestionsPopup.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        suggestionsPopup.add(new JScrollPane(suggestionList));

        txt_search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { showSuggestions(); }
            public void removeUpdate(DocumentEvent e) { showSuggestions(); }
            public void changedUpdate(DocumentEvent e) { showSuggestions(); }

            private void showSuggestions() {
                String input = txt_search.getText().trim().toLowerCase();
                if (input.isEmpty()) {
                    suggestionsPopup.setVisible(false);
                    return;
                }

                List<String> matches = new ArrayList<>();
                for (Sach sach : fullBookList) {
                    if (sach.getTenSach().toLowerCase().contains(input)) {
                        matches.add(sach.getTenSach());
                    }
                }

                if (matches.isEmpty()) {
                    suggestionsPopup.setVisible(false);
                    return;
                }

                suggestionList.setListData(matches.toArray(new String[0]));
                suggestionList.setSelectedIndex(0);

                suggestionsPopup.setPopupSize(txt_search.getWidth(), 150);
                suggestionsPopup.show(txt_search, 0, txt_search.getHeight());
            }
        });

        txt_search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && suggestionsPopup.isVisible()) {
                    String selected = suggestionList.getSelectedValue();
                    if (selected != null) {
                        txt_search.setText(selected);
                        suggestionsPopup.setVisible(false);
                        updateDisplayedBooksByKeyword(selected);
                    }
                }
            }
        });

        JButton btn_search = new JButton("🔍");
        btn_search.setPreferredSize(new Dimension(40, 40));
        btn_search.setMaximumSize(new Dimension(40, 40));
        btn_search.setMinimumSize(new Dimension(40, 40));
        btn_search.addActionListener(e -> {
            String keyword = txt_search.getText().trim();
            if (keyword.isEmpty()) {
                isFiltering = false;
                pnl_center.removeAll();
                displayedBooks.clear();
                loadMoreBooks();
            } else {
                updateDisplayedBooksByKeyword(keyword);
            }
        });
        pnl_top.add(btn_search);

        JButton btn_add = new JButton("Thêm");
        btn_add.setPreferredSize(new Dimension(80, 40));
        btn_add.setMaximumSize(new Dimension(80, 40));
        btn_add.setMinimumSize(new Dimension(80, 40));
        btn_add.addActionListener(e -> showAddBookForm(pnl_Main));
        pnl_top.add(btn_add);

        pnl_center = new JPanel();
        pnl_center.setLayout(new BoxLayout(pnl_center, BoxLayout.Y_AXIS));
        pnl_center.setBackground(Color.WHITE);
        pnl_center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Khởi tạo danh sách sách
        SachDao sachDao = SachDao.getInstance();
        fullBookList = sachDao.layDanhSach();
        displayedBooks = new ArrayList<>();

        // Hiển thị 20 cuốn sách đầu tiên (nếu có)
        loadMoreBooks();

        JScrollPane scrollPane = new JScrollPane(pnl_center);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                JScrollBar scrollBar = (JScrollBar) e.getSource();
                int extent = scrollBar.getModel().getExtent();
                int maximum = scrollBar.getModel().getMaximum();
                int value = scrollBar.getModel().getValue();
                if (value + extent >= maximum - 10) {
                    loadMoreBooks();
                }
            }
        });

        pnl_Main.add(pnl_top, BorderLayout.NORTH);
        pnl_Main.add(scrollPane, BorderLayout.CENTER);

        return pnl_Main;
    }

    private void loadMoreBooks() {
        if (isFiltering) return;

        int currentSize = displayedBooks.size();
        int booksToLoad = Math.min(fullBookList.size() - currentSize, BOOKS_PER_LOAD);

        if (booksToLoad > 0) {
            List<Sach> newBooks = fullBookList.subList(currentSize, currentSize + booksToLoad);
            displayedBooks.addAll(newBooks);

            JPanel rowPanel = new JPanel(new GridBagLayout());
            rowPanel.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;

            int count = 0;
            for (Sach book : newBooks) {
                gbc.gridx = count % 5;
                gbc.gridy = count / 5;

                JPanel pnl_card = createBookCard(book);
                rowPanel.add(pnl_card, gbc);

                count++;
            }

            pnl_center.add(rowPanel);
            pnl_center.revalidate();
            pnl_center.repaint();
        }
    }

    private void updateDisplayedBooksByKeyword(String keyword) {
        isFiltering = true;
        displayedBooks.clear();
        pnl_center.removeAll();

        JPanel rowPanel = null;
        int count = 0;

        for (Sach sach : fullBookList) {
            if (sach.getTenSach().toLowerCase().contains(keyword.toLowerCase())) {
                if (count % 5 == 0) {
                    rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
                    rowPanel.setBackground(Color.WHITE);
                    pnl_center.add(rowPanel);
                }

                JPanel pnl_card = createBookCard(sach);
                rowPanel.add(pnl_card);
                displayedBooks.add(sach);

                count++;
            }
        }

        pnl_center.revalidate();
        pnl_center.repaint();
    }

    private JPanel createBookCard(Sach book) {
        JPanel pnl_card = new JPanel();
        pnl_card.setLayout(new BorderLayout());
        pnl_card.setBackground(Color.WHITE);
        pnl_card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        pnl_card.setPreferredSize(new Dimension(120, 180));
        pnl_card.setMaximumSize(new Dimension(120, 180));
        pnl_card.setMinimumSize(new Dimension(120, 180));

        pnl_card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showEditBookForm(book, tabbedPane);
            }
        });

        JLabel lbl_image = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("pictures/" + book.getAnh());
            Image scaledImage = icon.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH);
            lbl_image.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            lbl_image.setText("No Image");
            lbl_image.setHorizontalAlignment(SwingConstants.CENTER);
        }
        lbl_image.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        lbl_image.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_image.setHorizontalAlignment(SwingConstants.CENTER);
        pnl_card.add(lbl_image, BorderLayout.NORTH);

        JLabel lbl_Title = new JLabel(book.getTenSach());
        lbl_Title.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_Title.setHorizontalAlignment(SwingConstants.CENTER);
        pnl_card.add(lbl_Title);

        return pnl_card;
    }

    private void showAddBookForm(JPanel pnl_Main) {
        List<TheLoai> categories = TheLoaiDao.getInstance().layDanhSach();
        List<String> selectedCategories = new ArrayList<>();
        final String[] selectedImagePath = {null};

        JLabel imagePreview = new JLabel();
        imagePreview.setPreferredSize(new Dimension(120, 150));
        imagePreview.setMaximumSize(new Dimension(120, 150));
        imagePreview.setMinimumSize(new Dimension(120, 150));
        imagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePreview.setHorizontalAlignment(SwingConstants.CENTER);
        imagePreview.setVerticalAlignment(SwingConstants.CENTER);
        imagePreview.setText("Chọn ảnh");

        imagePreview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedImagePath[0] = fileChooser.getSelectedFile().getAbsolutePath();
                    ImageIcon icon = new ImageIcon(selectedImagePath[0]);
                    Image scaled = icon.getImage().getScaledInstance(
                        imagePreview.getPreferredSize().width,
                        imagePreview.getPreferredSize().height,
                        Image.SCALE_SMOOTH
                    );
                    imagePreview.setIcon(new ImageIcon(scaled));
                    imagePreview.setText("");
                    pnl_Main.revalidate();
                    pnl_Main.repaint();
                }
            }
        });

        JPanel addBookPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblBookName = new JLabel("Tên sách");
        JTextField txtBookName = new JTextField(20);

        JLabel lblCategory = new JLabel("Thể loại");
        JTextField txtCategory = new JTextField();
        txtCategory.setEditable(false);

        JLabel lblPublisher = new JLabel("Nhà xuất bản");
        JTextField txtPublisher = new JTextField(20);

        JLabel lblYear = new JLabel("Năm xuất bản");
        JTextField txtYear = new JTextField(20);

        JLabel lblPrice = new JLabel("Giá");
        JTextField txtPrice = new JTextField(20);

        JButton btnAdd = new JButton("Thêm");
        JButton btnCancel = new JButton("Hủy");

        int row = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0; gbc.gridy = row;
        addBookPanel.add(lblBookName, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        addBookPanel.add(txtBookName, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0;
        addBookPanel.add(lblCategory, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        addBookPanel.add(txtCategory, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0;
        addBookPanel.add(lblPublisher, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        addBookPanel.add(txtPublisher, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0;
        addBookPanel.add(lblYear, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        addBookPanel.add(txtYear, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0;
        addBookPanel.add(lblPrice, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        addBookPanel.add(txtPrice, gbc);

        JPanel panelBtn = new JPanel();
        panelBtn.add(btnCancel);
        panelBtn.add(btnAdd);
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addBookPanel.add(panelBtn, gbc);

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setPreferredSize(new Dimension(140, 200));
        imagePanel.add(imagePreview);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(160, 240));
        leftPanel.add(imagePanel, BorderLayout.CENTER);

        pnl_Main.removeAll();
        pnl_Main.setLayout(new BorderLayout());
        pnl_Main.add(leftPanel, BorderLayout.WEST);
        pnl_Main.add(addBookPanel, BorderLayout.CENTER);
        pnl_Main.revalidate();
        pnl_Main.repaint();

        JPopupMenu popupMenu = new JPopupMenu();
        List<JCheckBoxMenuItem> categoryItems = new ArrayList<>();

        for (TheLoai tl : categories) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(tl.getTenTheLoai());
            item.addActionListener(ev -> {
                selectedCategories.clear();
                for (JCheckBoxMenuItem it : categoryItems) {
                    if (it.isSelected()) {
                        selectedCategories.add(it.getText());
                    }
                }
                txtCategory.setText(String.join(", ", selectedCategories));
            });
            categoryItems.add(item);
            popupMenu.add(item);
        }

        txtCategory.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupMenu.show(txtCategory, 0, txtCategory.getHeight());
            }
        });

        btnCancel.addActionListener(e -> {
            tabbedPane.setComponentAt(0, createBookPanel());
            tabbedPane.revalidate();
            tabbedPane.repaint();
        });

        btnAdd.addActionListener(e -> {
            if (txtBookName.getText().isEmpty() || selectedCategories.isEmpty() || txtPublisher.getText().isEmpty() ||
                txtYear.getText().isEmpty() || txtPrice.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            String maSach = generateBookCode();
            Sach newBook = new Sach();
            newBook.setMaSach(maSach);
            newBook.setTenSach(txtBookName.getText());
            newBook.setnXB(txtPublisher.getText());
            newBook.setNamXB(Integer.parseInt(txtYear.getText()));
            newBook.setGia(Double.parseDouble(txtPrice.getText()));
            newBook.setAnh(maSach + ".png");

            List<TheLoai> selectedTL = new ArrayList<>();
            for (String name : selectedCategories) {
                for (TheLoai tl : categories) {
                    if (tl.getTenTheLoai().equals(name)) {
                        selectedTL.add(tl);
                        break;
                    }
                }
            }
            newBook.setDsTheLoai(selectedTL);

            if (selectedImagePath[0] != null) {
                File src = new File(selectedImagePath[0]);
                File dest = new File("Pictures/" + maSach + ".png");
                try {
                    Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            SachDao.getInstance().addCategoriesToBook(maSach, selectedTL);
            SachDao.getInstance().themDoiTuong(newBook);
            JOptionPane.showMessageDialog(null, "Thêm sách thành công!");
            tabbedPane.setComponentAt(0, createBookPanel());
            tabbedPane.revalidate();
            tabbedPane.repaint();
        });
    }

    private void showEditBookForm(Sach book, JTabbedPane tabbedPane) {
        List<TheLoai> categories = TheLoaiDao.getInstance().layDanhSach();
        List<String> selectedCategories = new ArrayList<>();
        final String[] selectedImagePath = {null};

        JLabel imagePreview = new JLabel();
        imagePreview.setPreferredSize(new Dimension(120, 160));
        imagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePreview.setHorizontalAlignment(SwingConstants.CENTER);
        imagePreview.setVerticalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon("Pictures/" + book.getAnh());
            Image scaled = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
            imagePreview.setIcon(new ImageIcon(scaled));
        } catch (Exception ex) {
            imagePreview.setText("Chọn ảnh");
        }

        imagePreview.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedImagePath[0] = chooser.getSelectedFile().getAbsolutePath();
                    ImageIcon newIcon = new ImageIcon(selectedImagePath[0]);
                    Image scaled = newIcon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
                    imagePreview.setIcon(new ImageIcon(scaled));
                }
            }
        });

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblBookName = new JLabel("Tên sách");
        JTextField txtBookName = new JTextField(book.getTenSach(), 20);

        JLabel lblCategory = new JLabel("Thể loại");
        JTextField txtCategory = new JTextField();
        txtCategory.setEditable(false);

        JLabel lblPublisher = new JLabel("Nhà xuất bản");
        JTextField txtPublisher = new JTextField(book.getnXB(), 20);

        JLabel lblYear = new JLabel("Năm xuất bản");
        JTextField txtYear = new JTextField(String.valueOf(book.getNamXB()), 20);

        JLabel lblPrice = new JLabel("Giá");
        JTextField txtPrice = new JTextField(String.valueOf(book.getGia()), 20);

        int row = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(lblBookName, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtBookName, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0;
        formPanel.add(lblCategory, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtCategory, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0;
        formPanel.add(lblPublisher, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtPublisher, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0;
        formPanel.add(lblYear, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtYear, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0;
        formPanel.add(lblPrice, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtPrice, gbc);

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setPreferredSize(new Dimension(140, 200));
        imagePanel.add(imagePreview);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(160, 240));
        leftPanel.add(imagePanel, BorderLayout.CENTER);

        JPanel pnl_Main = new JPanel(new BorderLayout());
        pnl_Main.add(leftPanel, BorderLayout.WEST);
        pnl_Main.add(formPanel, BorderLayout.CENTER);

        for (TheLoai tl : book.getDsTheLoai()) {
            selectedCategories.add(tl.getTenTheLoai());
        }
        txtCategory.setText(String.join(", ", selectedCategories));

        JPopupMenu popupMenu = new JPopupMenu();
        List<JCheckBoxMenuItem> categoryItems = new ArrayList<>();
        for (TheLoai tl : categories) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(tl.getTenTheLoai());
            if (selectedCategories.contains(tl.getTenTheLoai())) item.setSelected(true);
            item.addActionListener(ev -> {
                selectedCategories.clear();
                for (JCheckBoxMenuItem it : categoryItems) {
                    if (it.isSelected()) {
                        selectedCategories.add(it.getText());
                    }
                }
                txtCategory.setText(String.join(", ", selectedCategories));
            });
            categoryItems.add(item);
            popupMenu.add(item);
        }
        txtCategory.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupMenu.show(txtCategory, 0, txtCategory.getHeight());
            }
        });

        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnCancel = new JButton("Hủy");
        JPanel panelBtn = new JPanel();
        panelBtn.add(btnUpdate);
        panelBtn.add(btnDelete);
        panelBtn.add(btnCancel);
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(panelBtn, gbc);

        btnCancel.addActionListener(e -> {
            tabbedPane.setComponentAt(0, createBookPanel());
            tabbedPane.revalidate();
            tabbedPane.repaint();
        });

        btnUpdate.addActionListener(e -> {
            if (txtBookName.getText().isEmpty() || selectedCategories.isEmpty() || txtPublisher.getText().isEmpty() ||
                txtYear.getText().isEmpty() || txtPrice.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            book.setTenSach(txtBookName.getText());
            book.setnXB(txtPublisher.getText());
            book.setNamXB(Integer.parseInt(txtYear.getText()));
            book.setGia(Double.parseDouble(txtPrice.getText()));
            book.setAnh(book.getMaSach() + ".png");

            List<TheLoai> selectedTL = new ArrayList<>();
            for (String name : selectedCategories) {
                for (TheLoai tl : categories) {
                    if (tl.getTenTheLoai().equals(name)) {
                        selectedTL.add(tl);
                        break;
                    }
                }
            }
            book.setDsTheLoai(selectedTL);

            if (selectedImagePath[0] != null) {
                try {
                    Files.copy(new File(selectedImagePath[0]).toPath(),
                            new File("Pictures/" + book.getMaSach() + ".png").toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            SachDao.getInstance().updateBookAndCategories(book);
            JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
            tabbedPane.setComponentAt(0, createBookPanel());
            tabbedPane.revalidate();
            tabbedPane.repaint();
        });

        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa sách này?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int ketQua = SachDao.getInstance().xoaDoiTuong(book);
                if (ketQua != 0) {
                    JOptionPane.showMessageDialog(null, "Đã xóa sách.");
                    tabbedPane.setComponentAt(0, createBookPanel());
                    tabbedPane.revalidate();
                    tabbedPane.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Sách đang được mượn không thể xóa");
                }
            }
        });

        tabbedPane.setComponentAt(0, pnl_Main);
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

    private String generateBookCode() {
        String lastBookCode = SachDao.getInstance().getLastBookCode();
        int nextCode = Integer.parseInt(lastBookCode.substring(1)) + 1;
        return "S" + String.format("%04d", nextCode);
    }

    // User Panel
    private JPanel createUserPanel() {
        JPanel pnl_Main = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã Người Dùng", "Tên Người Dùng", "Tài Khoản", "Email", "Số Điện Thoại", "Ngày Tạo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        final JTable table = new JTable(tableModel);
        loadTableData("");

        JPanel pnl_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl_top.setBackground(Color.WHITE);
        JTextField txt_search = new JTextField(25);
        txt_search.setPreferredSize(new Dimension(0, 40));
        txt_search.setMaximumSize(new Dimension(300, 40));
        pnl_top.add(txt_search);

        JButton btn_search = new JButton("🔍");
        btn_search.setPreferredSize(new Dimension(40, 40));
        btn_search.setMaximumSize(new Dimension(40, 40));
        btn_search.setMinimumSize(new Dimension(40, 40));
        btn_search.addActionListener(e -> {
            String keyword = txt_search.getText().trim();
            loadTableData(keyword);
        });
        pnl_top.add(btn_search);

        JButton btn_add = new JButton("Thêm");
        btn_add.setPreferredSize(new Dimension(80, 40));
        btn_add.setMaximumSize(new Dimension(80, 40));
        btn_add.setMinimumSize(new Dimension(80, 40));
        btn_add.addActionListener(e -> {
            JTextField tenNguoiDungField = new JTextField(10);
            JTextField taiKhoanField = new JTextField(10);
            JTextField matKhauField = new JTextField(10);
            JTextField emailField = new JTextField(10);
            JTextField soDienThoaiField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Tên Người Dùng:"));
            panel.add(tenNguoiDungField);
            panel.add(new JLabel("Tài Khoản:"));
            panel.add(taiKhoanField);
            panel.add(new JLabel("Mật Khẩu:"));
            panel.add(matKhauField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Số Điện Thoại:"));
            panel.add(soDienThoaiField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Độc Giả", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String tenNguoiDung = tenNguoiDungField.getText();
                String taiKhoan = taiKhoanField.getText();
                String matKhauGoc = matKhauField.getText();
                String matKhauHash = hashSHA1(matKhauGoc);
                String email = emailField.getText();
                String soDienThoai = soDienThoaiField.getText();

                if (tenNguoiDung.isEmpty() || taiKhoan.isEmpty() || matKhauGoc.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Tên, tài khoản và mật khẩu không được để trống!");
                    return;
                }

                try {
                    DocGiaDao docGiaDao = DocGiaDao.getInstance();
                    String maNguoiDung = docGiaDao.layMaNguoiDungMoiNhat();
                    Date ngayTao = new Date();

                    DocGia docGia = new DocGia(maNguoiDung, tenNguoiDung, taiKhoan, matKhauHash, email, soDienThoai, ngayTao);
                    int rowsAffected = docGiaDao.themDoiTuong(docGia);
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Thêm độc giả thành công!");
                        loadTableData("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm độc giả thất bại!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm độc giả: " + ex.getMessage());
                }
            }
        });
        pnl_top.add(btn_add);

        JButton btn_delete = new JButton("Xóa");
        btn_delete.setPreferredSize(new Dimension(80, 40));
        btn_delete.setMaximumSize(new Dimension(80, 40));
        btn_delete.setMinimumSize(new Dimension(80, 40));
        btn_delete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String maNguoiDung = (String) tableModel.getValueAt(selectedRow, 0);
                String tenNguoiDung = (String) tableModel.getValueAt(selectedRow, 1);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc chắn muốn xóa độc giả '" + tenNguoiDung + "' (Mã: " + maNguoiDung + ") không?\n" +
                        "Các thông tin liên quan đến độc giả này sẽ được xóa toàn bộ.",
                        "Xác nhận xóa độc giả",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        DocGiaDao docGiaDao = DocGiaDao.getInstance();
                        DocGia tmp = new DocGia();
                        tmp.setMaNguoiDung(maNguoiDung);
                        int rowsAffected = docGiaDao.xoaDoiTuong(tmp);
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Xóa độc giả thành công!");
                            loadTableData("");
                        } else {
                            JOptionPane.showMessageDialog(this, "Xóa độc giả thất bại! Không tìm thấy độc giả hoặc lỗi cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Lỗi khi xóa độc giả: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một độc giả để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        pnl_top.add(btn_delete);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnl_Main.add(pnl_top, BorderLayout.NORTH);
        pnl_Main.add(scrollPane, BorderLayout.CENTER);

        return pnl_Main;
    }

    private String hashSHA1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashed = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Không thể hash mật khẩu", e);
        }
    }

    // Borrow Panel
    private JPanel createBorrowPanel() {
        JPanel panelMain = new JPanel(new BorderLayout());
        JTextField txt_Search = new JTextField(25);
        pnl_MainContent = new JPanel();
        pnl_MainContent.setLayout(new BoxLayout(pnl_MainContent, BoxLayout.Y_AXIS));
        pnl_MainContent.setBackground(Color.WHITE);
        List<Ls_Dg_sach> ds = Ls_Dg_sachDao.getInstance().layDanhSach();
        filteredDs = ds != null ? new ArrayList<>(ds) : new ArrayList<>();

        // Initialize pagination buttons
        btn_prev = new JButton("Trước");
        btn_prev.setPreferredSize(new Dimension(80, 40));
        btn_prev.setMaximumSize(new Dimension(80, 40));
        btn_prev.setMinimumSize(new Dimension(80, 40));
        btn_prev.setVisible(currentPage > 1);
        btn_prev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                updatePageContent();
            }
        });

        btn_next = new JButton("Sau");
        btn_next.setPreferredSize(new Dimension(80, 40));
        btn_next.setMaximumSize(new Dimension(80, 40));
        btn_next.setMinimumSize(new Dimension(80, 40));
        btn_next.setVisible(ds != null && ds.size() > itemsPerPage);
        btn_next.addActionListener(e -> {
            if ((currentPage * itemsPerPage) < filteredDs.size()) {
                currentPage++;
                updatePageContent();
            }
        });

        JPanel pnl_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl_top.setBackground(Color.WHITE);
        txt_Search.setPreferredSize(new Dimension(0, 40));
        txt_Search.setMaximumSize(new Dimension(300, 40));
        txt_Search.setText("Tìm mã KH, tên sách");
        txt_Search.setForeground(Color.GRAY);

        txt_Search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txt_Search.getText().equals("Tìm mã KH, tên sách")) {
                    txt_Search.setText("");
                    txt_Search.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txt_Search.getText().isEmpty()) {
                    txt_Search.setText("Tìm mã KH, tên sách");
                    txt_Search.setForeground(Color.GRAY);
                }
            }
        });

        pnl_top.add(txt_Search);

        JButton btn_search = new JButton("Tìm");
        btn_search.setPreferredSize(new Dimension(80, 40));
        btn_search.setMaximumSize(new Dimension(80, 40));
        btn_search.setMinimumSize(new Dimension(80, 40));
        btn_search.addActionListener(e -> {
            String query = txt_Search.getText().trim();
            if (query.equals("Tìm mã KH, tên sách") || query.isEmpty()) {
                filteredDs = new ArrayList<>(ds);
            } else {
                filteredDs = new ArrayList<>();
                String lowerQuery = query.toLowerCase();
                for (Ls_Dg_sach ls : ds) {
                    if (ls.getMaNguoiDung().toLowerCase().contains(lowerQuery) ||
                        ls.getTenSach().toLowerCase().contains(lowerQuery)) {
                        filteredDs.add(ls);
                    }
                }
            }
            currentPage = 1;
            updatePageContent();
        });
        pnl_top.add(btn_search);

        JButton btn_add = new JButton("Thêm");
        btn_add.setPreferredSize(new Dimension(80, 40));
        btn_add.setMaximumSize(new Dimension(80, 40));
        btn_add.setMinimumSize(new Dimension(80, 40));
        btn_add.addActionListener(e -> showAddBorrowForm(panelMain));
        pnl_top.add(btn_add);

        JButton btn_filter = new JButton("Lọc");
        btn_filter.setPreferredSize(new Dimension(80, 40));
        btn_filter.setMaximumSize(new Dimension(80, 40));
        btn_filter.setMinimumSize(new Dimension(80, 40));

        JPopupMenu filterMenu = new JPopupMenu();
        JMenuItem itemChuaTra = new JMenuItem("Chưa trả");
        JMenuItem itemDaTra = new JMenuItem("Đã trả");
        JMenuItem itemTatCa = new JMenuItem("Tất cả");
        filterMenu.add(itemChuaTra);
        filterMenu.add(itemDaTra);
        filterMenu.add(itemTatCa);

        itemChuaTra.addActionListener(e -> {
            filteredDs = new ArrayList<>();
            for (Ls_Dg_sach ls : ds) {
                if (ls.getTrangThai().equals("Chưa trả")) {
                    filteredDs.add(ls);
                }
            }
            currentPage = 1;
            updatePageContent();
        });

        itemDaTra.addActionListener(e -> {
            filteredDs = new ArrayList<>();
            for (Ls_Dg_sach ls : ds) {
                if (ls.getTrangThai().equals("Đã trả")) {
                    filteredDs.add(ls);
                }
            }
            currentPage = 1;
            updatePageContent();
        });

        itemTatCa.addActionListener(e -> {
            filteredDs = new ArrayList<>(ds);
            currentPage = 1;
            updatePageContent();
        });

        btn_filter.addActionListener(e -> {
            filterMenu.show(btn_filter, 0, btn_filter.getHeight());
        });

        pnl_top.add(btn_filter);

        panelMain.add(pnl_top, BorderLayout.NORTH);

        scrollPane = new JScrollPane(pnl_MainContent);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(900, 550));

        updatePageContent();

        panelMain.add(scrollPane, BorderLayout.CENTER);

        JPanel pnl_pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl_pagination.setBackground(Color.WHITE);
        pnl_pagination.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_pagination.add(btn_prev);
        pnl_pagination.add(btn_next);

        panelMain.add(pnl_pagination, BorderLayout.SOUTH);

        return panelMain;
    }

    private void showAddBorrowForm(JPanel panelMain) {
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblTitle = new JLabel("Thêm Lịch Sử Mượn Sách");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        addPanel.add(new JLabel("Mã lịch sử:"), gbc);
        gbc.gridx = 1;
        String maLichSu = LichSuMuonSachDao.getInstance().generateMaLichSu();
        JTextField txtMaLichSu = new JTextField(maLichSu, 20);
        txtMaLichSu.setEditable(false);
        addPanel.add(txtMaLichSu, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Mã sách:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaSach = new JTextField(20);
        addPanel.add(txtMaSach, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Mã thủ thư:"), gbc);
        gbc.gridx = 1;
        String maThuThu = getCurrentLibrarianCode();
        JTextField txtMaThuThu = new JTextField(maThuThu, 20);
        txtMaThuThu.setEditable(false);
        addPanel.add(txtMaThuThu, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Mã độc giả:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaDocGia = new JTextField(20);
        addPanel.add(txtMaDocGia, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Ngày mượn (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JTextField txtNgayMuon = new JTextField(sdf.format(new Date()), 20);
        addPanel.add(txtNgayMuon, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Ngày trả (yyyy-MM-dd, để trống nếu chưa trả):"), gbc);
        gbc.gridx = 1;
        JTextField txtNgayTra = new JTextField(20);
        addPanel.add(txtNgayTra, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        JTextField txtTrangThai = new JTextField("Chưa trả", 20);
        txtTrangThai.setEditable(false);
        addPanel.add(txtTrangThai, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            try {
                String maSach = txtMaSach.getText().trim();
                String maThuThuInput = txtMaThuThu.getText().trim();
                String maDocGia = txtMaDocGia.getText().trim();
                String ngayMuonStr = txtNgayMuon.getText().trim();
                String ngayTraStr = txtNgayTra.getText().trim();
                String trangThai = txtTrangThai.getText().trim();

                if (maSach.isEmpty() || maThuThuInput.isEmpty() || maDocGia.isEmpty() || ngayMuonStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã sách, mã thủ thư, mã độc giả và ngày mượn!");
                    return;
                }

                if (maLichSu.length() > 5) {
                    JOptionPane.showMessageDialog(this, "Mã lịch sử quá dài, tối đa 5 ký tự!");
                    return;
                }

                SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date ngayMuon;
                try {
                    ngayMuon = new java.sql.Date(sdfParse.parse(ngayMuonStr).getTime());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ngày mượn không đúng định dạng (yyyy-MM-dd)!");
                    return;
                }
                java.sql.Date ngayTra = null;
                if (!ngayTraStr.isEmpty()) {
                    try {
                        ngayTra = new java.sql.Date(sdfParse.parse(ngayTraStr).getTime());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Ngày trả không đúng định dạng (yyyy-MM-dd)!");
                        return;
                    }
                }

                LichSuMuonSach ls = new LichSuMuonSach(maLichSu, ngayMuon, ngayTra, trangThai, maSach, maThuThuInput, maDocGia);

                int result = LichSuMuonSachDao.getInstance().themDoiTuong(ls);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm lịch sử mượn sách thành công!");
                    tabbedPane.setComponentAt(2, createBorrowPanel());
                    tabbedPane.revalidate();
                    tabbedPane.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại! Vui lòng kiểm tra lại dữ liệu.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });
        addPanel.add(btnSave, gbc);

        gbc.gridx = 1;
        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> {
            tabbedPane.setComponentAt(2, createBorrowPanel());
            tabbedPane.revalidate();
            tabbedPane.repaint();
        });
        addPanel.add(btnCancel, gbc);

        panelMain.removeAll();
        panelMain.add(addPanel, BorderLayout.CENTER);
        panelMain.revalidate();
        panelMain.repaint();
    }

    private String getCurrentLibrarianCode() {
        String fullName = LoginSession.getInstance().getFullName();
        List<ThuThu> librarians = ThuThuDao.getInstance().layDanhSachTheoDK(fullName);
        if (!librarians.isEmpty()) {
            return librarians.get(0).getMaNguoiDung();
        }
        return "UNKNOWN";
    }

    private void showEditBorrowForm(Ls_Dg_sach lsDg, JPanel panelMain) {
        LichSuMuonSach ls = LichSuMuonSachDao.getInstance().getByMaLichSu(lsDg.getMaLichSu());

        if (ls == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy bản ghi lịch sử mượn sách!");
            return;
        }

        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblTitle = new JLabel("Sửa Lịch Sử Mượn Sách");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        editPanel.add(new JLabel("Mã lịch sử:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaLichSu = new JTextField(ls.getMaLichSu(), 20);
        txtMaLichSu.setEditable(false);
        editPanel.add(txtMaLichSu, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Mã sách:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaSach = new JTextField(ls.getMaSach(), 20);
        editPanel.add(txtMaSach, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Mã thủ thư:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaThuThu = new JTextField(ls.getMaThuThu(), 20);
        editPanel.add(txtMaThuThu, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Mã độc giả:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaDocGia = new JTextField(ls.getMaDocGia(), 20);
        editPanel.add(txtMaDocGia, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Ngày mượn (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JTextField txtNgayMuon = new JTextField(sdf.format(ls.getNgayMuon()), 20);
        editPanel.add(txtNgayMuon, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Ngày trả (yyyy-MM-dd, để trống nếu chưa trả):"), gbc);
        gbc.gridx = 1;
        JTextField txtNgayTra = new JTextField(ls.getNgayTra() != null ? sdf.format(ls.getNgayTra()) : "", 20);
        editPanel.add(txtNgayTra, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        String[] trangThaiOptions = {"Chưa trả", "Đã trả"};
        JComboBox<String> cbTrangThai = new JComboBox<>(trangThaiOptions);
        cbTrangThai.setSelectedItem(ls.getTrangThai());
        cbTrangThai.setPreferredSize(new Dimension(200, 25));
        editPanel.add(cbTrangThai, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            try {
                String maSach = txtMaSach.getText().trim();
                String maThuThu = txtMaThuThu.getText().trim();
                String maDocGia = txtMaDocGia.getText().trim();
                String ngayMuonStr = txtNgayMuon.getText().trim();
                String ngayTraStr = txtNgayTra.getText().trim();
                String trangThai = (String) cbTrangThai.getSelectedItem();

                if (maSach.isEmpty() || maThuThu.isEmpty() || maDocGia.isEmpty() || ngayMuonStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã sách, mã thủ thư, mã độc giả và ngày mượn!");
                    return;
                }

                SimpleDateFormat sdfUpdate = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date ngayMuon;
                try {
                    ngayMuon = new java.sql.Date(sdfUpdate.parse(ngayMuonStr).getTime());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ngày mượn không đúng định dạng (yyyy-MM-dd)!");
                    return;
                }
                java.sql.Date ngayTra = null;
                if (!ngayTraStr.isEmpty()) {
                    try {
                        ngayTra = new java.sql.Date(sdfUpdate.parse(ngayTraStr).getTime());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Ngày trả không đúng định dạng (yyyy-MM-dd)!");
                        return;
                    }
                }

                LichSuMuonSach lsUpdated = new LichSuMuonSach(
                    ls.getMaLichSu(), ngayMuon, ngayTra, trangThai, maSach, maThuThu, maDocGia
                );

                int result = LichSuMuonSachDao.getInstance().capNhatDoiTuong(lsUpdated);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Cập nhật lịch sử mượn sách thành công!");
                    tabbedPane.setComponentAt(2, createBorrowPanel());
                    tabbedPane.revalidate();
                    tabbedPane.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại! Vui lòng kiểm tra lại dữ liệu.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });
        editPanel.add(btnSave, gbc);

        gbc.gridx = 1;
        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> {
            tabbedPane.setComponentAt(2, createBorrowPanel());
            tabbedPane.revalidate();
            tabbedPane.repaint();
        });
        editPanel.add(btnCancel, gbc);

        panelMain.removeAll();
        panelMain.add(editPanel, BorderLayout.CENTER);
        panelMain.revalidate();
        panelMain.repaint();
    }

    private JPanel createBookItem(Ls_Dg_sach ls, Font itemFont, Font itemBoldFont, JPanel panelMain) {
        RoundedPanel itemPanel = new RoundedPanel(20);
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setBackground(new Color(182, 162, 162));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPanel.setBorder(new EmptyBorder(5, 10, 5, 15));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(150, 140));

        leftPanel.add(Box.createVerticalStrut(10));

        ImageIcon bookIcon = new ImageIcon("Pictures/" + ls.getAnh());
        if (bookIcon.getIconWidth() != -1) {
            Image scaledBook = bookIcon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
            JLabel lblBook = new JLabel(new ImageIcon(scaledBook));
            lblBook.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(lblBook);
        }

        String bookName = "<html><div style='text-align: center; width: 140px;'>" + ls.getTenSach() + "</div></html>";
        JLabel lblBookName = new JLabel(bookName);
        lblBookName.setFont(itemBoldFont);
        lblBookName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBookName.setForeground(Color.BLACK);

        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(lblBookName);

        leftPanel.add(Box.createVerticalStrut(10));

        itemPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 5, 1, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        rightPanel.add(createInfoRow("Tên:", ls.getTenNguoiDung(), itemFont, itemBoldFont), gbc);
        gbc.gridy++;
        rightPanel.add(createInfoRow("Mã KH:", ls.getMaNguoiDung(), itemFont, itemBoldFont), gbc);
        gbc.gridy++;
        rightPanel.add(createInfoRow("Ngày mượn:", ls.getNgayMuon().toString(), itemFont, itemBoldFont), gbc);
        gbc.gridy++;
        rightPanel.add(createInfoRow("Ngày trả:", ls.getNgayTra() == null ? "Chưa có" : ls.getNgayTra().toString(), itemFont, itemBoldFont), gbc);
        gbc.gridy++;
        rightPanel.add(createStatusRow("Trạng thái:", ls.getTrangThai(), itemFont, itemBoldFont), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 5, 1, 5);
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setOpaque(false);

        JButton btnEdit = new JButton("Sửa");
        btnEdit.setPreferredSize(new Dimension(70, 30));
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEdit.setBackground(new Color(204, 204, 204));
        btnEdit.setOpaque(true);
        btnEdit.setBorderPainted(true);
        btnEdit.addActionListener(e -> showEditBorrowForm(ls, panelMain));
        actionPanel.add(btnEdit);

        JButton btnDelete = new JButton("Xóa");
        btnDelete.setPreferredSize(new Dimension(70, 30));
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDelete.setBackground(new Color(204, 204, 204));
        btnDelete.setOpaque(true);
        btnDelete.setBorderPainted(true);
        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa lịch sử mượn sách với mã " + ls.getMaLichSu() + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                LichSuMuonSach lsToDelete = new LichSuMuonSach(
                    ls.getMaLichSu(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                );
                int result = LichSuMuonSachDao.getInstance().xoaDoiTuong(lsToDelete);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa lịch sử mượn sách thành công!");
                    tabbedPane.setComponentAt(2, createBorrowPanel());
                    tabbedPane.revalidate();
                    tabbedPane.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại! Vui lòng thử lại.");
                }
            }
        });
        actionPanel.add(btnDelete);

        rightPanel.add(actionPanel, gbc);

        itemPanel.add(rightPanel, BorderLayout.CENTER);

        return itemPanel;
    }

    private JPanel createInfoRow(String label, String value, Font itemFont, Font itemBoldFont) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        panel.setOpaque(false);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(itemBoldFont);
        lblLabel.setForeground(Color.BLACK);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(itemFont);
        lblValue.setForeground(Color.BLACK);

        panel.add(lblLabel);
        panel.add(lblValue);
        return panel;
    }

    private JPanel createStatusRow(String label, String status, Font itemFont, Font itemBoldFont) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        panel.setOpaque(false);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(itemBoldFont);
        lblLabel.setForeground(Color.BLACK);

        JLabel lblStatus = new JLabel(status);
        lblStatus.setFont(itemBoldFont);
        lblStatus.setForeground(status.equals("Chưa trả") ? Color.RED : Color.GREEN);

        panel.add(lblLabel);
        panel.add(lblStatus);
        return panel;
    }

    // Penalty Ticket Info Panel (Placeholder)
    private JPanel createPenaltyTicketInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Chức năng Thông tin phiếu phạt chưa được triển khai", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // Statistics Panel
    private JPanel createStatisticsPanel() {
        JPanel pnl_Main = new JPanel(new BorderLayout());
        pnl_cards = new JPanel();
        currencyFormat = new DecimalFormat("#,##0.00");
        currencyFormat.setGroupingSize(3);

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

    private JPanel createStatsPanel(ThongTinThongKe thongKe) {
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

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);
        JLabel lblThangNam = new JLabel(String.format("%02d/%d", thongKe.getThang(), Year.now().getValue()));
        lblThangNam.setFont(new Font("Arial", Font.BOLD, 14));
        lblThangNam.setForeground(Color.BLACK);
        lblThangNam.setBackground(Color.WHITE);
        lblThangNam.setOpaque(true);
        lblThangNam.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        headerPanel.add(lblThangNam);
        card.add(headerPanel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

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

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblDoanhThu = new JLabel("Tổng thu:");
        lblDoanhThu.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDoanhThu.setForeground(Color.WHITE);
        statsPanel.add(lblDoanhThu, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel valDoanhThu = new JLabel(String.valueOf(thongKe.getDoanhThu()));
        valDoanhThu.setFont(new Font("Arial", Font.BOLD, 14));
        valDoanhThu.setForeground(Color.WHITE);
        statsPanel.add(valDoanhThu, gbc);

        card.add(statsPanel, BorderLayout.CENTER);
        return card;
    }

    private void loadTableData(String keyword) {
        tableModel.setRowCount(0);
        DocGiaDao docGiaDao = DocGiaDao.getInstance();
        List<DocGia> docGiaList;
        if (keyword.isEmpty()) {
            docGiaList = docGiaDao.layDanhSach();
        } else {
            docGiaList = docGiaDao.timKiem(keyword);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (DocGia docGia : docGiaList) {
            Object[] row = {
                docGia.getMaNguoiDung(),
                docGia.getTenNguoiDung(),
                docGia.getTaiKhoan(),
                docGia.getEmail(),
                docGia.getSoDienThoai(),
                sdf.format(docGia.getNgayTao())
            };
            tableModel.addRow(row);
        }
    }

    private void updatePageContent() {
        pnl_MainContent.removeAll();
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, filteredDs.size());
        
        for (int i = startIndex; i < endIndex; i++) {
            Ls_Dg_sach ls = filteredDs.get(i);
            JPanel itemPanel = createBookItem(ls, itemFont, itemBoldFont, pnl_MainContent);
            pnl_MainContent.add(itemPanel);
        }
        
        pnl_MainContent.revalidate();
        pnl_MainContent.repaint();

        scrollPane.getVerticalScrollBar().setValue(0);
        btn_prev.setVisible(currentPage > 1);
        btn_next.setVisible((currentPage * itemsPerPage) < filteredDs.size());
    }

    private void updateStatisticsPanel(int year) {
        pnl_cards.removeAll();
        List<ThongTinThongKe> ds = DAO.ThongTinThongKeDao.getInstance().layDanhSachTheoNam(year);
        
        for (ThongTinThongKe tt : ds) {
            JPanel statsPanel = createStatsPanel(tt);
            pnl_cards.add(statsPanel);
        }
        
        pnl_cards.revalidate();
        pnl_cards.repaint();
    }

    private class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }
}
