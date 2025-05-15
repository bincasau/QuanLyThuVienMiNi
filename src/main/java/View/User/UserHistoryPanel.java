package View.User;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import DAO.DocGiaDao;
import DAO.LichSuMuonSachDao;
import DAO.SachDao;
import Model.LichSuMuonSach;
import Model.Sach;
import Model.DocGia;

public class UserHistoryPanel extends JPanel {
    private String maDocGia;
    private JPanel pnl_ContentArea;
    private List<LichSuMuonSach> lichSuList;
    private JTextField txt_Search;
    private JButton btn_ClearSearch;

    public UserHistoryPanel(String maDocGia) {
        this.maDocGia = maDocGia;
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
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy dữ liệu: " + e.getMessage());
            e.printStackTrace();
            lichSuList = List.of();
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel pnl_SearchRow = new JPanel();
        pnl_SearchRow.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnl_SearchRow.setBackground(Color.WHITE);
        pnl_SearchRow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txt_Search = new JTextField("Tìm kiếm mã sách, tên sách, ngày mượn, ngày trả...", 20);
        txt_Search.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txt_Search.setForeground(Color.GRAY);
        txt_Search.setPreferredSize(new Dimension(300, 40));

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

        txt_Search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (txt_Search.isEditable()) {
                    updateHistoryContent(txt_Search.getText(), null);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (txt_Search.isEditable()) {
                    updateHistoryContent(txt_Search.getText(), null);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (txt_Search.isEditable()) {
                    updateHistoryContent(txt_Search.getText(), null);
                }
            }
        });

        txt_Search.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    txt_Search.setEditable(false);
                    btn_ClearSearch.setVisible(true);
                }
            }
        });

        btn_ClearSearch = new JButton("✕");
        btn_ClearSearch.setPreferredSize(new Dimension(40, 40));
        btn_ClearSearch.setVisible(false);
        btn_ClearSearch.addActionListener(e -> {
            txt_Search.setText("Tìm kiếm mã sách, tên sách, ngày mượn, ngày trả...");
            txt_Search.setForeground(Color.GRAY);
            txt_Search.setEditable(true);
            btn_ClearSearch.setVisible(false);
            updateHistoryContent(null, null);
        });

        JButton btn_Borrowing = new JButton("Đang mượn");
        btn_Borrowing.setPreferredSize(new Dimension(120, 40));
        btn_Borrowing.setFocusable(false);
        btn_Borrowing.addActionListener(e -> updateHistoryContent(txt_Search.getText(), "Chưa trả"));

        JButton btn_Returned = new JButton("Đã trả");
        btn_Returned.setPreferredSize(new Dimension(120, 40));
        btn_Returned.setFocusable(false);
        btn_Returned.addActionListener(e -> updateHistoryContent(txt_Search.getText(), "Đã trả"));

        pnl_SearchRow.add(txt_Search);
        pnl_SearchRow.add(btn_ClearSearch);
        pnl_SearchRow.add(btn_Borrowing);
        pnl_SearchRow.add(btn_Returned);

        add(pnl_SearchRow, BorderLayout.NORTH);

        pnl_ContentArea = new JPanel();
        pnl_ContentArea.setLayout(new BoxLayout(pnl_ContentArea, BoxLayout.Y_AXIS));
        pnl_ContentArea.setBackground(Color.WHITE);
        pnl_ContentArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(pnl_ContentArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        updateHistoryContent(null, null);
    }

    private void updateHistoryContent(String searchText, String filter) {
        pnl_ContentArea.removeAll();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        boolean hasData = false;
        SachDao sachDao = SachDao.getInstance();
        List<LichSuMuonSach> filteredList = new ArrayList<>();

        for (LichSuMuonSach lichSu : lichSuList) {
            String maSach = lichSu.getMaSach();
            String tenSach = "Không tìm thấy";
            List<Sach> sachList = sachDao.layDanhSachTheoDK(maSach);
            if (!sachList.isEmpty()) {
                tenSach = sachList.get(0).getTenSach();
            }

            String ngayMuon = (lichSu.getNgayMuon() != null) ? dateFormat.format(lichSu.getNgayMuon()) : "";
            String ngayTra = (lichSu.getNgayTra() != null) ? dateFormat.format(lichSu.getNgayTra()) : "";
            String trangThai = lichSu.getTrangThai();

            boolean matchesSearch = searchText == null ||
                    searchText.equals("Tìm kiếm mã sách, tên sách, ngày mượn, ngày trả...") ||
                    maSach.toLowerCase().contains(searchText.toLowerCase()) ||
                    tenSach.toLowerCase().contains(searchText.toLowerCase()) ||
                    ngayMuon.contains(searchText) ||
                    ngayTra.contains(searchText);

            boolean matchesFilter = filter == null || trangThai.equals(filter);

            if (matchesSearch && matchesFilter) {
                filteredList.add(lichSu);
            }
        }

        for (LichSuMuonSach lichSu : filteredList) {
            hasData = true;
            String maSach = lichSu.getMaSach();
            String tenSach = "Không tìm thấy";
            List<Sach> sachList = sachDao.layDanhSachTheoDK(maSach);
            if (!sachList.isEmpty()) {
                tenSach = sachList.get(0).getTenSach();
            }
            String ngayMuon = (lichSu.getNgayMuon() != null) ? dateFormat.format(lichSu.getNgayMuon()) : "N/A";
            String ngayTra = (lichSu.getNgayTra() != null) ? dateFormat.format(lichSu.getNgayTra()) : "N/A";
            String bookTitle = "Tên sách: " + tenSach + " (" + maSach + ")";
            boolean isBorrowing = "Chưa trả".equals(lichSu.getTrangThai());
            JPanel itemPanel = createHistoryItem(bookTitle, "Mượn: " + ngayMuon + ", Trả: " + ngayTra, isBorrowing);
            pnl_ContentArea.add(itemPanel);
            pnl_ContentArea.add(Box.createRigidArea(new Dimension(0, 5)));
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
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lbl_BookTitle = new JLabel(bookTitle);
        lbl_BookTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl_BookTitle.setPreferredSize(new Dimension(300, 25));

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
}