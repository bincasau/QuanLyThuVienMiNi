package View.User;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import DAO.SachDao;
import DAO.TheLoaiDao;
import Model.Sach;
import Model.TheLoai;

public class Dashboard extends JPanel {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String fullName;
    private List<Sach> bookList;
    private List<Sach> fullBookList;
    private int currentBookIndex = 0;
    private static final int BATCH_SIZE = 20;
    private JPanel pnl_Content;
    private JFrame parentFrame;

    public Dashboard(String fullName, JFrame parentFrame) {
        this.fullName = fullName;
        this.parentFrame = parentFrame;
        initializeUI();
    }

    public Dashboard(JFrame parentFrame) {
        this.fullName = "Guest";
        this.parentFrame = parentFrame;
        initializeUI();
    }

    private void initializeUI() {
        setSize(1000, 600);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

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

        contentPane.add(scrollPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(contentPane, BorderLayout.CENTER);
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
        bookList = new ArrayList<>(fullBookList);

        loadMoreBooks();
        return pnl_Content;
    }

    private void loadMoreBooks() {
        int endIndex = Math.min(currentBookIndex + BATCH_SIZE, bookList.size());
        int count = 0;
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        rowPanel.setBackground(Color.WHITE);
        for (int i = currentBookIndex; i < endIndex; i++) {
            Sach book = bookList.get(i);
            JPanel card = createBookCard(book);
            rowPanel.add(card);
            count++;
            if (count % 5 == 0) {
                pnl_Content.add(rowPanel);
                rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
                rowPanel.setBackground(Color.WHITE);
            }
        }
        if (rowPanel.getComponentCount() > 0) {
            pnl_Content.add(rowPanel);
        }
        currentBookIndex = endIndex;
        pnl_Content.revalidate();
        pnl_Content.repaint();
    }

    private JPanel createBookCard(Sach book) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setPreferredSize(new Dimension(120, 180));

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon("pictures/" + book.getAnh());
            Image scaledImage = icon.getImage().getScaledInstance(80, 120, Image.SCALE_SMOOTH);
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

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showBookDetails(book);
            }
        });

        return card;
    }

    private void showBookDetails(Sach book) {
        JDialog detailDialog = new JDialog(parentFrame, "Thông tin sách", true);
        detailDialog.setSize(500, 350);
        detailDialog.setLocationRelativeTo(parentFrame);
        detailDialog.setLayout(new BorderLayout(15, 15));
        detailDialog.getContentPane().setBackground(Color.WHITE);
        detailDialog.setResizable(false);

        JLabel imgLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("pictures/" + book.getAnh());
            Image img = icon.getImage().getScaledInstance(160, 220, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imgLabel.setText("No Image");
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        imgLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 10));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 15));

        addInfoLabel(infoPanel, "Tên sách:", book.getTenSach(), 18, true);
        addInfoLabel(infoPanel, "Mã sách:", book.getMaSach(), 14, false);
        addInfoLabel(infoPanel, "Nhà xuất bản:", book.getnXB(), 14, false);
        addInfoLabel(infoPanel, "Năm xuất bản:", String.valueOf(book.getNamXB()), 14, false);

        String theLoai = (book.getDsTheLoai() != null && !book.getDsTheLoai().isEmpty())
                ? book.getDsTheLoai().stream().map(TheLoai::getTenTheLoai).reduce((a, b) -> a + ", " + b).get()
                : "Không rõ";
        addInfoLabel(infoPanel, "Thể loại:", theLoai, 14, false);

        detailDialog.add(imgLabel, BorderLayout.WEST);
        detailDialog.add(infoPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Đóng");
        closeButton.setBackground(new Color(107, 142, 35));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusable(false);
        closeButton.setPreferredSize(new Dimension(80, 35));
        closeButton.addActionListener(e -> detailDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);

        detailDialog.add(buttonPanel, BorderLayout.SOUTH);

        detailDialog.setVisible(true);
    }

    private void addInfoLabel(JPanel panel, String title, String content, int fontSize, boolean bold) {
        JLabel lbl = new JLabel("<html><b>" + title + "</b> " + content + "</html>");
        lbl.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, fontSize));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(lbl);
    }

    public void openFilterDialog() {
        JDialog filterDialog = new JDialog(parentFrame, "Lọc theo thể loại", true);
        filterDialog.setSize(300, 400);
        filterDialog.setLocationRelativeTo(parentFrame);
        filterDialog.setLayout(new BorderLayout());

        List<TheLoai> allGenres = TheLoaiDao.getInstance().layDanhSach();

        JPanel genrePanel = new JPanel();
        genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.Y_AXIS));
        genrePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Map<String, JCheckBox> checkBoxMap = new HashMap<>();
        for (TheLoai genre : allGenres) {
            JCheckBox cb = new JCheckBox(genre.getTenTheLoai());
            cb.setAlignmentX(Component.LEFT_ALIGNMENT);
            genrePanel.add(cb);
            checkBoxMap.put(genre.getTenTheLoai(), cb);
        }

        JScrollPane scrollPane = new JScrollPane(genrePanel);
        filterDialog.add(scrollPane, BorderLayout.CENTER);

        JButton btnApply = new JButton("Áp dụng");
        btnApply.setBackground(new Color(107, 142, 35));
        btnApply.setForeground(Color.WHITE);
        btnApply.setFocusable(false);
        btnApply.setPreferredSize(new Dimension(100, 30));

        btnApply.addActionListener(e -> {
            applyGenreFilter(checkBoxMap);
            filterDialog.dispose();
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnApply);
        filterDialog.add(btnPanel, BorderLayout.SOUTH);

        filterDialog.setVisible(true);
    }

    public void applyGenreFilter(Map<String, JCheckBox> checkBoxMap) {
        List<String> selectedGenres = checkBoxMap.entrySet().stream()
                .filter(entry -> entry.getValue().isSelected())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (selectedGenres.isEmpty()) {
            bookList = new ArrayList<>(fullBookList);
        } else {
            bookList = fullBookList.stream()
                    .filter(sach -> sach.getDsTheLoai() != null &&
                            sach.getDsTheLoai().stream()
                                    .map(TheLoai::getTenTheLoai)
                                    .anyMatch(selectedGenres::contains))
                    .collect(Collectors.toList());
        }

        currentBookIndex = 0;
        pnl_Content.removeAll();
        loadMoreBooks();
    }

    public void search(String keyword) {
        SwingUtilities.invokeLater(() -> {
            if (keyword.trim().isEmpty() || keyword.equals("Nhập Tên Sách")) {
                bookList = new ArrayList<>(fullBookList);
            } else {
                bookList = fullBookList.stream()
                        .filter(s -> s.getTenSach().toLowerCase().contains(keyword.toLowerCase().trim()))
                        .collect(Collectors.toList());
            }
            currentBookIndex = 0;
            pnl_Content.removeAll();
            loadMoreBooks();
        });
    }

    public void clearFilters() {
        bookList = new ArrayList<>(fullBookList);
        currentBookIndex = 0;
        pnl_Content.removeAll();
        loadMoreBooks();
    }
}