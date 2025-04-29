package View.Librarian;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DAO.DocGiaDao;
import Model.DocGia;

public class User extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel pnl_content;
    private JTable table;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                User frame = new User();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public User() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600); // Tăng chiều rộng để chứa thêm cột
        setTitle("Thư viện mini - Quản lý độc giả");
        setLocationRelativeTo(null);

        pnl_content = new JPanel(new BorderLayout());
        pnl_content.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(pnl_content);

        JPanel pnl_Sidebar = createSidebar();
        pnl_content.add(pnl_Sidebar, BorderLayout.WEST);

        JPanel pnl_Main = createMain();
        pnl_Main.setBackground(Color.WHITE);
        pnl_content.add(pnl_Main);

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
        btn_User.setBackground(new Color(150, 130, 130));
        btn_User.setOpaque(true);
        pnl_Sidebar.add(btn_User);

        JButton btn_Borrow = new JButton("Mượn sách");
        btn_Borrow.setPreferredSize(new Dimension(200, 50));
        btn_Borrow.setMaximumSize(new Dimension(200, 50));
        btn_Borrow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn_Borrow.setOpaque(true);
        pnl_Sidebar.add(btn_Borrow);

        JButton btn_PenaltyTicketInfo = new JButton("Thông tin phiếu phạt");
        btn_PenaltyTicketInfo.setPreferredSize(new Dimension(200, 50));
        btn_PenaltyTicketInfo.setMaximumSize(new Dimension(200, 50));
        btn_PenaltyTicketInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn_PenaltyTicketInfo.setOpaque(true);
        pnl_Sidebar.add(btn_PenaltyTicketInfo);

        JButton btn_Statistics = new JButton("Thống kê");
        btn_Statistics.setPreferredSize(new Dimension(200, 50));
        btn_Statistics.setMaximumSize(new Dimension(200, 50));
        btn_Statistics.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn_Statistics.setOpaque(true);
        pnl_Sidebar.add(btn_Statistics);

        pnl_Sidebar.add(Box.createVerticalGlue());

        JButton btn_Logout = new JButton("Đăng xuất");
        btn_Logout.setPreferredSize(new Dimension(200, 50));
        btn_Logout.setMaximumSize(new Dimension(200, 50));
        btn_Logout.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn_Logout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                View.Login.Login loginForm = new View.Login.Login();
                loginForm.setVisible(true);
            });
        });
        pnl_Sidebar.add(btn_Logout);

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
        lbl_Title.setBackground(new Color(106, 85, 85));
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

        // Phần tìm kiếm và nút chức năng
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
            JTextField maNguoiDungField = new JTextField(10);
            JTextField tenNguoiDungField = new JTextField(10);
            JTextField taiKhoanField = new JTextField(10);
            JTextField matKhauField = new JTextField(10);
            JTextField emailField = new JTextField(10);
            JTextField soDienThoaiField = new JTextField(10);
            JTextField ngayTaoField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(7, 2));
            panel.add(new JLabel("Mã Người Dùng:"));
            panel.add(maNguoiDungField);
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
            panel.add(new JLabel("Ngày Tạo (yyyy-MM-dd):"));
            panel.add(ngayTaoField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Độc Giả", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String maNguoiDung = maNguoiDungField.getText();
                String tenNguoiDung = tenNguoiDungField.getText();
                String taiKhoan = taiKhoanField.getText();
                String matKhau = matKhauField.getText();
                String email = emailField.getText();
                String soDienThoai = soDienThoaiField.getText();
                String ngayTaoStr = ngayTaoField.getText();

                if (maNguoiDung.isEmpty() || tenNguoiDung.isEmpty() || taiKhoan.isEmpty() || matKhau.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Mã, tên, tài khoản và mật khẩu không được để trống!");
                    return;
                }

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date ngayTao = sdf.parse(ngayTaoStr);

                    DocGia docGia = new DocGia(maNguoiDung, tenNguoiDung, taiKhoan, matKhau, email, soDienThoai, ngayTao);
                    DocGiaDao docGiaDao = new DocGiaDao();
                    int rowsAffected = docGiaDao.themDoiTuong(docGia);
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Thêm độc giả thành công!");
                        loadTableData(""); // Cập nhật lại bảng
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm độc giả thất bại!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ngày tạo không hợp lệ! Vui lòng nhập theo định dạng yyyy-MM-dd.");
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
                int confirm = JOptionPane.showConfirmDialog(this, "Xóa độc giả " + maNguoiDung + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DocGiaDao docGiaDao = new DocGiaDao();
                    int rowsAffected = docGiaDao.xoaDoiTuong(maNguoiDung);
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa độc giả thành công!");
                        loadTableData(""); // Cập nhật lại bảng
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa độc giả thất bại!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một độc giả để xóa!");
            }
        });
        pnl_top.add(btn_delete);

        // Bảng hiển thị danh sách độc giả
        String[] columnNames = {"Mã Người Dùng", "Tên Người Dùng", "Tài Khoản", "Mật Khẩu", "Email", "Số Điện Thoại", "Ngày Tạo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        loadTableData(""); // Tải dữ liệu ban đầu

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnl_Main.add(pnl_top, BorderLayout.NORTH);
        pnl_Main.add(scrollPane, BorderLayout.CENTER);

        return pnl_Main;
    }

    private void loadTableData(String keyword) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        DocGiaDao docGiaDao = new DocGiaDao();
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
                docGia.getMatKhau(),
                docGia.getEmail(),
                docGia.getSoDienThoai(),
                sdf.format(docGia.getNgayTao())
            };
            tableModel.addRow(row);
        }
    }
}