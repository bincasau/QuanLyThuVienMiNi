package View.Librarian;

import DAO.DocGiaDao;
import DAO.PhieuPhatDao;
import Model.PhieuPhat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ThongTinPhieuPhat extends JFrame {
    private JPanel pnl_Content;
    private JTextField txt_Search;
    private JPopupMenu suggestionsPopup;
    private JList<String> suggestionList;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private List<PhieuPhat> ds;
    private List<PhieuPhat> filteredDs;
    private int currentPage = 1;
    private final int ITEMS_PER_PAGE = 20;
    private JButton btn_prev;
    private JButton btn_next;
    private final String PLACEHOLDER_TEXT = "Tìm mã phiếu phạt, lý do, tên độc giả";
    private JPanel pnl_ListContent;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ThongTinPhieuPhat frame = new ThongTinPhieuPhat();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ThongTinPhieuPhat() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setTitle("Thư viện mini - Quản lý phiếu phạt");
        setLocationRelativeTo(null);

        pnl_Content = new JPanel(new BorderLayout());
        pnl_Content.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(pnl_Content);

        JPanel pnl_Sidebar = createSidebar();
        pnl_Content.add(pnl_Sidebar, BorderLayout.WEST);

        JPanel pnl_Header = createHeader();
        pnl_Content.add(pnl_Header, BorderLayout.NORTH);

        pnl_ListContent = createContent();
        pnl_Content.add(pnl_ListContent, BorderLayout.CENTER);
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
            if (name.equals("Thông tin phiếu phạt")) {
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

        JLabel lbl_Title = new JLabel("NHÀ SÁCH MINI");
        lbl_Title.setFont(new Font("Arial", Font.BOLD, 24));
        lbl_Title.setForeground(Color.WHITE);
        lbl_Title.setBorder(null);
        pnl_Header.add(lbl_Title);
        pnl_Header.add(Box.createHorizontalGlue());

        JLabel lbl_LibrarianName = new JLabel("Thủ thư: Nguyễn Văn A");
        lbl_LibrarianName.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl_LibrarianName.setForeground(Color.WHITE);
        lbl_LibrarianName.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        pnl_Header.add(lbl_LibrarianName);

        Dimension size = new Dimension(50, 50);

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

        JPanel pnl_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl_top.setBackground(Color.WHITE);

        txt_Search = new JTextField(25);
        txt_Search.setPreferredSize(new Dimension(300, 40));
        txt_Search.setMaximumSize(new Dimension(300, 40));
        txt_Search.setText(PLACEHOLDER_TEXT);
        txt_Search.setForeground(Color.GRAY);

        suggestionsPopup = new JPopupMenu();
        suggestionList = new JList<>();
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionsPopup.setFocusable(false);
        suggestionsPopup.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        suggestionsPopup.add(new JScrollPane(suggestionList));

        txt_Search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txt_Search.getText().equals(PLACEHOLDER_TEXT)) {
                    txt_Search.setText("");
                    txt_Search.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txt_Search.getText().isEmpty()) {
                    txt_Search.setText(PLACEHOLDER_TEXT);
                    txt_Search.setForeground(Color.GRAY);
                }
            }
        });

        txt_Search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { showSuggestions(); }
            public void removeUpdate(DocumentEvent e) { showSuggestions(); }
            public void changedUpdate(DocumentEvent e) { showSuggestions(); }

            private void showSuggestions() {
                String input = txt_Search.getText().trim().toLowerCase();
                if (input.isEmpty() || input.equals(PLACEHOLDER_TEXT.toLowerCase())) {
                    suggestionsPopup.setVisible(false);
                    return;
                }

                List<String> matches = new ArrayList<>();
                for (PhieuPhat pp : ds) {
                    String tenDocGia = DocGiaDao.getInstance().getTenNguoiDungByMaNguoiDung(pp.getMaDocGia());
                    if (pp.getMaPhieuPhat().toLowerCase().contains(input) && !matches.contains(pp.getMaPhieuPhat())) {
                        matches.add(pp.getMaPhieuPhat());
                    }
                    if (pp.getLoi().toLowerCase().contains(input) && !matches.contains(pp.getLoi())) {
                        matches.add(pp.getLoi());
                    }
                    if (tenDocGia != null && tenDocGia.toLowerCase().contains(input) && !matches.contains(tenDocGia)) {
                        matches.add(tenDocGia);
                    }
                }

                if (matches.isEmpty()) {
                    suggestionsPopup.setVisible(false);
                    return;
                }

                suggestionList.setListData(matches.toArray(new String[0]));
                suggestionList.setSelectedIndex(0);
                suggestionsPopup.setPopupSize(txt_Search.getWidth(), 150);
                suggestionsPopup.show(txt_Search, 0, txt_Search.getHeight());
            }
        });

        txt_Search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && suggestionsPopup.isVisible()) {
                    String selected = suggestionList.getSelectedValue();
                    if (selected != null) {
                        txt_Search.setText(selected);
                        suggestionsPopup.setVisible(false);
                        updateDisplayedFinesByKeyword(selected);
                    }
                }
            }
        });

        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selected = suggestionList.getSelectedValue();
                    if (selected != null) {
                        txt_Search.setText(selected);
                        suggestionsPopup.setVisible(false);
                        updateDisplayedFinesByKeyword(selected);
                    }
                }
            }
        });

        pnl_top.add(txt_Search);

        JButton btn_search = new JButton("Tìm");
        btn_search.setPreferredSize(new Dimension(80, 40));
        btn_search.addActionListener(e -> {
            String keyword = txt_Search.getText().trim();
            if (keyword.equals(PLACEHOLDER_TEXT) || keyword.isEmpty()) {
                filteredDs = new ArrayList<>(ds);
                currentPage = 1;
                updateTableContent();
            } else {
                updateDisplayedFinesByKeyword(keyword);
            }
        });
        pnl_top.add(btn_search);

        JButton btn_add = new JButton("Thêm");
        btn_add.setPreferredSize(new Dimension(80, 40));
        btn_add.addActionListener(e -> showAddForm());
        pnl_top.add(btn_add);

        JButton btn_edit = new JButton("Sửa");
        btn_edit.setPreferredSize(new Dimension(80, 40));
        btn_edit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String maPhieuPhat = (String) tableModel.getValueAt(selectedRow, 0);
                PhieuPhat pp = PhieuPhatDao.getInstance().getByMaPhieuPhat(maPhieuPhat);
                if (pp != null) {
                    showEditForm(pp);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu phạt!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu phạt để sửa!");
            }
        });
        pnl_top.add(btn_edit);

        JButton btn_delete = new JButton("Xóa");
        btn_delete.setPreferredSize(new Dimension(80, 40));
        btn_delete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String maPhieuPhat = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa phiếu phạt với mã " + maPhieuPhat + "?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    PhieuPhat ppToDelete = new PhieuPhat(maPhieuPhat, null, 0, null, null, null);
                    int result = PhieuPhatDao.getInstance().xoaDoiTuong(ppToDelete);
                    if (result > 0) {
                        ds = PhieuPhatDao.getInstance().layDanhSach();
                        filteredDs = ds != null ? new ArrayList<>(ds) : new ArrayList<>();
                        currentPage = Math.min(currentPage, (filteredDs.size() + ITEMS_PER_PAGE - 1) / ITEMS_PER_PAGE);
                        updateTableContent();
                        JOptionPane.showMessageDialog(this, "Xóa phiếu phạt thành công!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại! Vui lòng thử lại.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu phạt để xóa!");
            }
        });
        pnl_top.add(btn_delete);

        panelMain.add(pnl_top, BorderLayout.NORTH);

        String[] columnNames = {"Mã phiếu phạt", "Lý do", "Số tiền (VND)", "Tên độc giả", "Mã sách", "Ngày lập phiếu"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(900, 550));

        ds = PhieuPhatDao.getInstance().layDanhSach();
        filteredDs = ds != null ? new ArrayList<>(ds) : new ArrayList<>();

        btn_prev = new JButton("Trước");
        btn_prev.setPreferredSize(new Dimension(80, 40));
        btn_next = new JButton("Sau");
        btn_next.setPreferredSize(new Dimension(80, 40));

        updateTableContent();

        panelMain.add(scrollPane, BorderLayout.CENTER);

        JPanel pnl_pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl_pagination.setBackground(Color.WHITE);
        pnl_pagination.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btn_prev.setVisible(currentPage > 1);
        btn_prev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                updateTableContent();
            }
        });
        pnl_pagination.add(btn_prev);

        btn_next.setVisible((currentPage * ITEMS_PER_PAGE) < filteredDs.size());
        btn_next.addActionListener(e -> {
            if ((currentPage * ITEMS_PER_PAGE) < filteredDs.size()) {
                currentPage++;
                updateTableContent();
            }
        });
        pnl_pagination.add(btn_next);

        panelMain.add(pnl_pagination, BorderLayout.SOUTH);

        return panelMain;
    }

    private void updateDisplayedFinesByKeyword(String keyword) {
        filteredDs = new ArrayList<>();
        String lowerQuery = keyword.toLowerCase();
        for (PhieuPhat pp : ds) {
            String tenDocGia = DocGiaDao.getInstance().getTenNguoiDungByMaNguoiDung(pp.getMaDocGia());
            if (pp.getMaPhieuPhat().toLowerCase().contains(lowerQuery) ||
                pp.getLoi().toLowerCase().contains(lowerQuery) ||
                (tenDocGia != null && tenDocGia.toLowerCase().contains(lowerQuery))) {
                filteredDs.add(pp);
            }
        }
        currentPage = 1;
        updateTableContent();
    }

    private void updateTableContent() {
        tableModel.setRowCount(0);

        // Sắp xếp filteredDs theo ngày lập phiếu (mới nhất trước)
        filteredDs.sort((pp1, pp2) -> pp2.getNgayPhieu().compareTo(pp1.getNgayPhieu()));

        int startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, filteredDs.size());

        for (int i = startIndex; i < endIndex; i++) {
            PhieuPhat pp = filteredDs.get(i);
            String tenDocGia = DocGiaDao.getInstance().getTenNguoiDungByMaNguoiDung(pp.getMaDocGia());
            tableModel.addRow(new Object[]{
                pp.getMaPhieuPhat(),
                pp.getLoi(),
                String.format("%.2f", pp.getGiaTien()),
                tenDocGia != null ? tenDocGia : "Không tìm thấy",
                pp.getMaSach(),
                pp.getNgayPhieu() != null ? new SimpleDateFormat("yyyy-MM-dd").format(pp.getNgayPhieu()) : "N/A"
            });
        }

        if (btn_prev != null) btn_prev.setVisible(currentPage > 1);
        if (btn_next != null) btn_next.setVisible((currentPage * ITEMS_PER_PAGE) < filteredDs.size());

        table.revalidate();
        table.repaint();
    }

    private void showAddForm() {
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridBagLayout());
        addPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblTitle = new JLabel("Thêm Phiếu Phạt");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        addPanel.add(new JLabel("Mã phiếu phạt:"), gbc);
        gbc.gridx = 1;
        String maPhieuPhat = PhieuPhatDao.getInstance().generateMaPhieuPhat();
        JTextField txtMaPhieuPhat = new JTextField(maPhieuPhat, 20);
        txtMaPhieuPhat.setEditable(false);
        addPanel.add(txtMaPhieuPhat, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Lý do vi phạm:"), gbc);
        gbc.gridx = 1;
        JTextField txtLoi = new JTextField(20);
        addPanel.add(txtLoi, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Số tiền phạt (VND):"), gbc);
        gbc.gridx = 1;
        JTextField txtGiaTien = new JTextField(20);
        addPanel.add(txtGiaTien, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Mã sách:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaSach = new JTextField(20);
        addPanel.add(txtMaSach, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Ngày lập phiếu:"), gbc);
        gbc.gridx = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JTextField txtNgayPhieu = new JTextField(sdf.format(new Date()), 20);
        txtNgayPhieu.setEditable(false);
        addPanel.add(txtNgayPhieu, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            try {
                String loi = txtLoi.getText().trim();
                String giaTienStr = txtGiaTien.getText().trim();
                String maSach = txtMaSach.getText().trim();
                String ngayPhieuStr = txtNgayPhieu.getText().trim();

                if (loi.isEmpty() || giaTienStr.isEmpty() || maSach.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                double giaTien;
                try {
                    giaTien = Double.parseDouble(giaTienStr);
                    if (giaTien < 0) {
                        JOptionPane.showMessageDialog(this, "Số tiền phạt phải lớn hơn hoặc bằng 0!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số tiền phạt phải là số hợp lệ!");
                    return;
                }

                SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date ngayPhieu = new java.sql.Date(sdfParse.parse(ngayPhieuStr).getTime());

                PhieuPhat pp = new PhieuPhat(maPhieuPhat, loi, giaTien, null, maSach, ngayPhieu);

                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn thêm phiếu phạt với mã " + maPhieuPhat + "?",
                    "Xác nhận thêm",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    int result = PhieuPhatDao.getInstance().themDoiTuong(pp);
                    pnl_Content.removeAll();
                    pnl_Content.add(createSidebar(), BorderLayout.WEST);
                    pnl_Content.add(createHeader(), BorderLayout.NORTH);
                    pnl_Content.add(pnl_ListContent, BorderLayout.CENTER);
                    if (result > 0) {
                        ds = PhieuPhatDao.getInstance().layDanhSach();
                        filteredDs = ds != null ? new ArrayList<>(ds) : new ArrayList<>();
                        currentPage = 1;
                        updateTableContent();
                        JOptionPane.showMessageDialog(this, "Thêm phiếu phạt thành công!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại! Vui lòng kiểm tra lại dữ liệu.");
                    }
                    pnl_Content.revalidate();
                    pnl_Content.repaint();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });
        addPanel.add(btnSave, gbc);

        gbc.gridx = 1;
        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> {
            pnl_Content.removeAll();
            pnl_Content.add(createSidebar(), BorderLayout.WEST);
            pnl_Content.add(createHeader(), BorderLayout.NORTH);
            pnl_Content.add(pnl_ListContent, BorderLayout.CENTER);
            pnl_Content.revalidate();
            pnl_Content.repaint();
        });
        addPanel.add(btnCancel, gbc);

        pnl_Content.removeAll();
        pnl_Content.add(createSidebar(), BorderLayout.WEST);
        pnl_Content.add(createHeader(), BorderLayout.NORTH);
        pnl_Content.add(addPanel, BorderLayout.CENTER);
        pnl_Content.revalidate();
        pnl_Content.repaint();
    }

    private void showEditForm(PhieuPhat pp) {
        PhieuPhat phieuPhat = PhieuPhatDao.getInstance().getByMaPhieuPhat(pp.getMaPhieuPhat());

        if (phieuPhat == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu phạt!");
            return;
        }

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        editPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblTitle = new JLabel("Sửa Phiếu Phạt");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        editPanel.add(new JLabel("Mã phiếu phạt:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaPhieuPhat = new JTextField(phieuPhat.getMaPhieuPhat(), 20);
        txtMaPhieuPhat.setEditable(false);
        editPanel.add(txtMaPhieuPhat, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Lý do vi phạm:"), gbc);
        gbc.gridx = 1;
        JTextField txtLoi = new JTextField(phieuPhat.getLoi(), 20);
        editPanel.add(txtLoi, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Số tiền phạt (VND):"), gbc);
        gbc.gridx = 1;
        JTextField txtGiaTien = new JTextField(String.valueOf(phieuPhat.getGiaTien()), 20);
        editPanel.add(txtGiaTien, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Mã sách:"), gbc);
        gbc.gridx = 1;
        JTextField txtMaSach = new JTextField(phieuPhat.getMaSach(), 20);
        editPanel.add(txtMaSach, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Ngày lập phiếu:"), gbc);
        gbc.gridx = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JTextField txtNgayPhieu = new JTextField(phieuPhat.getNgayPhieu() != null ? sdf.format(phieuPhat.getNgayPhieu()) : sdf.format(new Date()), 20);
        txtNgayPhieu.setEditable(false);
        editPanel.add(txtNgayPhieu, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            try {
                String loi = txtLoi.getText().trim();
                String giaTienStr = txtGiaTien.getText().trim();
                String maSach = txtMaSach.getText().trim();
                String ngayPhieuStr = txtNgayPhieu.getText().trim();

                if (loi.isEmpty() || giaTienStr.isEmpty() || maSach.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                double giaTien;
                try {
                    giaTien = Double.parseDouble(giaTienStr);
                    if (giaTien < 0) {
                        JOptionPane.showMessageDialog(this, "Số tiền phạt phải lớn hơn hoặc bằng 0!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số tiền phạt phải là số hợp lệ!");
                    return;
                }

                SimpleDateFormat sdfUpdate = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date ngayPhieu = new java.sql.Date(sdfUpdate.parse(ngayPhieuStr).getTime());

                PhieuPhat ppUpdated = new PhieuPhat(
                    phieuPhat.getMaPhieuPhat(), loi, giaTien, null, maSach, ngayPhieu
                );

                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn sửa phiếu phạt với mã " + phieuPhat.getMaPhieuPhat() + "?",
                    "Xác nhận sửa",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    int result = PhieuPhatDao.getInstance().capNhatDoiTuong(ppUpdated);
                    pnl_Content.removeAll();
                    pnl_Content.add(createSidebar(), BorderLayout.WEST);
                    pnl_Content.add(createHeader(), BorderLayout.NORTH);
                    pnl_Content.add(pnl_ListContent, BorderLayout.CENTER);
                    if (result > 0) {
                        ds = PhieuPhatDao.getInstance().layDanhSach();
                        filteredDs = ds != null ? new ArrayList<>(ds) : new ArrayList<>();
                        currentPage = 1;
                        updateTableContent();
                        JOptionPane.showMessageDialog(this, "Cập nhật phiếu phạt thành công!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại! Vui lòng kiểm tra lại dữ liệu.");
                    }
                    pnl_Content.revalidate();
                    pnl_Content.repaint();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });
        editPanel.add(btnSave, gbc);

        gbc.gridx = 1;
        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> {
            pnl_Content.removeAll();
            pnl_Content.add(createSidebar(), BorderLayout.WEST);
            pnl_Content.add(createHeader(), BorderLayout.NORTH);
            pnl_Content.add(pnl_ListContent, BorderLayout.CENTER);
            pnl_Content.revalidate();
            pnl_Content.repaint();
        });
        editPanel.add(btnCancel, gbc);

        pnl_Content.removeAll();
        pnl_Content.add(createSidebar(), BorderLayout.WEST);
        pnl_Content.add(createHeader(), BorderLayout.NORTH);
        pnl_Content.add(editPanel, BorderLayout.CENTER);
        pnl_Content.revalidate();
        pnl_Content.repaint();
    }
}