package View.Login;

import Controller.LoginController;
import View.Librarian.Book;
import View.User.Dashboard;
import View.MainFrame;
import Session.LoginSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame {

    private boolean isTaiKhoanPlaceholderActive = true;
    private boolean isMatKhauPlaceholderActive = true;
    private LoginController loginController;
    private JTextField txt_TaiKhoan;
    private JPasswordField txt_MatKhau;
    private JPanel pnl_DangNhap;

    // Cập nhật placeholder cho trường nhập liệu
    private void updatePlaceholder(Component field, String placeholder, boolean forcePlaceholder) {
        SwingUtilities.invokeLater(() -> {
            if (field == txt_TaiKhoan) {
                String text = txt_TaiKhoan.getText();
                if (forcePlaceholder || text.isEmpty()) {
                    txt_TaiKhoan.setText(placeholder);
                    txt_TaiKhoan.setForeground(Color.GRAY);
                    isTaiKhoanPlaceholderActive = true;
                } else if (!text.equals(placeholder)) {
                    txt_TaiKhoan.setForeground(Color.BLACK);
                    isTaiKhoanPlaceholderActive = false;
                }
            } else if (field == txt_MatKhau) {
                String text = new String(txt_MatKhau.getPassword());
                if (forcePlaceholder || text.isEmpty()) {
                    txt_MatKhau.setText(placeholder);
                    txt_MatKhau.setForeground(Color.GRAY);
                    isMatKhauPlaceholderActive = true;
                } else if (!text.equals(placeholder)) {
                    txt_MatKhau.setForeground(Color.BLACK);
                    isMatKhauPlaceholderActive = false;
                }
            }
            pnl_DangNhap.repaint();
        });
    }

    public Login() {
        loginController = new LoginController();

        setTitle("Thư Viện MINI - Đăng Nhập");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chính
        pnl_DangNhap = new JPanel(null);
        pnl_DangNhap.setBackground(new Color(244, 231, 228));

        // Tiêu đề
        JLabel lbl_Title = new JLabel("Thư Viện MINI", SwingConstants.CENTER);
        lbl_Title.setFont(new Font("Arial", Font.BOLD, 28));
        lbl_Title.setBounds(0, 50, 1000, 40);
        pnl_DangNhap.add(lbl_Title);

        // Avatar
        JLabel lbl_Avatar = new JLabel();
        try {
            ImageIcon avatarIcon = new ImageIcon("Pictures/avatar.png");
            if (avatarIcon.getIconWidth() == -1) throw new Exception("Hình ảnh không tải được");
            Image scaledImage = avatarIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lbl_Avatar.setIcon(new ImageIcon(scaledImage));
            lbl_Avatar.setBounds(440, 100, 120, 120);
        } catch (Exception e) {
            lbl_Avatar.setOpaque(true);
            lbl_Avatar.setBackground(Color.GRAY);
            lbl_Avatar.setBounds(440, 100, 120, 120);
            System.err.println("Lỗi tải avatar: " + e.getMessage());
        }
        pnl_DangNhap.add(lbl_Avatar);

        // Tài khoản
        JLabel lbl_TaiKhoan = new JLabel("Tài Khoản");
        lbl_TaiKhoan.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl_TaiKhoan.setBounds(300, 250, 100, 30);
        pnl_DangNhap.add(lbl_TaiKhoan);

        txt_TaiKhoan = new JTextField("VD: HuyenPhuThuan");
        txt_TaiKhoan.setFont(new Font("Arial", Font.ITALIC, 14));
        txt_TaiKhoan.setForeground(Color.GRAY);
        txt_TaiKhoan.setBounds(300, 280, 400, 40);
        txt_TaiKhoan.setCaretPosition(0);
        pnl_DangNhap.add(txt_TaiKhoan);

        // Mật khẩu
        JLabel lbl_MatKhau = new JLabel("Mật Khẩu");
        lbl_MatKhau.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl_MatKhau.setBounds(300, 330, 100, 30);
        pnl_DangNhap.add(lbl_MatKhau);

        txt_MatKhau = new JPasswordField("VD: abc12345");
        txt_MatKhau.setFont(new Font("Arial", Font.ITALIC, 14));
        txt_MatKhau.setForeground(Color.GRAY);
        txt_MatKhau.setBounds(300, 360, 400, 40);
        txt_MatKhau.setCaretPosition(0);
        pnl_DangNhap.add(txt_MatKhau);

        // Nút đăng nhập
        JButton btn_DangNhap = new JButton("Đăng nhập");
        btn_DangNhap.setBounds(300, 430, 400, 50);
        btn_DangNhap.setBackground(new Color(166, 107, 107));
        btn_DangNhap.setForeground(Color.WHITE);
        btn_DangNhap.setFocusPainted(false);
        btn_DangNhap.setFont(new Font("Arial", Font.PLAIN, 16));
        pnl_DangNhap.add(btn_DangNhap);

        // Xử lý placeholder cho Tài khoản
        txt_TaiKhoan.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isTaiKhoanPlaceholderActive) {
                    txt_TaiKhoan.setText("");
                    txt_TaiKhoan.setForeground(Color.BLACK);
                    isTaiKhoanPlaceholderActive = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                updatePlaceholder(txt_TaiKhoan, "VD: HuyenPhuThuan", false);
            }
        });

        txt_TaiKhoan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (isTaiKhoanPlaceholderActive) {
                    txt_TaiKhoan.setText("");
                    txt_TaiKhoan.setForeground(Color.BLACK);
                    isTaiKhoanPlaceholderActive = false;
                }
            }
        });

        // Xử lý placeholder cho Mật khẩu
        txt_MatKhau.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isMatKhauPlaceholderActive) {
                    txt_MatKhau.setText("");
                    txt_MatKhau.setForeground(Color.BLACK);
                    isMatKhauPlaceholderActive = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                updatePlaceholder(txt_MatKhau, "VD: abc12345", false);
            }
        });

        txt_MatKhau.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (isMatKhauPlaceholderActive) {
                    txt_MatKhau.setText("");
                    txt_MatKhau.setForeground(Color.BLACK);
                    isMatKhauPlaceholderActive = false;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btn_DangNhap.doClick();
                }
            }
        });

        // Xử lý nút Đăng nhập
        btn_DangNhap.addActionListener(e -> {
            System.out.println("Login button clicked");
            
            // Validate input
            if (isTaiKhoanPlaceholderActive || txt_TaiKhoan.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(Login.this, 
                    "Vui lòng nhập tên đăng nhập", 
                    "Lỗi đăng nhập", 
                    JOptionPane.ERROR_MESSAGE);
                txt_TaiKhoan.requestFocus();
                return;
            }
            
            if (isMatKhauPlaceholderActive || txt_MatKhau.getPassword().length == 0) {
                JOptionPane.showMessageDialog(Login.this, 
                    "Vui lòng nhập mật khẩu", 
                    "Lỗi đăng nhập", 
                    JOptionPane.ERROR_MESSAGE);
                txt_MatKhau.requestFocus();
                return;
            }

            String username = txt_TaiKhoan.getText().trim();
            String password = new String(txt_MatKhau.getPassword());
            
            System.out.println("Attempting login with username: " + username);

            try {
                loginController.login(username, password, new LoginController.LoginCallBack() {
                    @Override
                    public void onSuccess(String username, boolean isAdmin) {
                        LoginSession.getInstance().login(username, isAdmin);
                        dispose();
                        SwingUtilities.invokeLater(() -> {
                            new MainFrame().setVisible(true);
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        System.out.println("Login failed: " + message);
                        JOptionPane.showMessageDialog(Login.this, message, "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        System.out.println("System error: " + errorMessage);
                        JOptionPane.showMessageDialog(Login.this, errorMessage, "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                System.err.println("Unexpected error during login: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(Login.this, 
                    "Đã xảy ra lỗi không mong muốn: " + ex.getMessage(), 
                    "Lỗi hệ thống", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        add(pnl_DangNhap);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}