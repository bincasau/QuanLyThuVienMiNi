package View.Login;

import Controller.LoginController; // Thêm Controller Login
import View.Librarian.Librarian;
import View.User.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame {

    private boolean isTaiKhoanPlaceholderActive = true;
    private boolean isMatKhauPlaceholderActive = true;
    private LoginController loginController;

    // Phương thức cập nhật trạng thái placeholder cho ô "Tài Khoản"
    private void updateTaiKhoanPlaceholder(JTextField textField, boolean forcePlaceholder) {
        SwingUtilities.invokeLater(() -> {
            if (forcePlaceholder || textField.getText().isEmpty()) {
                textField.setText("VD: HuyenPhuThuan");
                textField.setForeground(Color.GRAY);
                textField.setCaretPosition(0);
                isTaiKhoanPlaceholderActive = true;
            } else if (!textField.getText().equals("VD: HuyenPhuThuan")) {
                textField.setForeground(Color.BLACK);
                isTaiKhoanPlaceholderActive = false;
            }
            textField.invalidate();
            textField.repaint();
        });
    }

    // Phương thức cập nhật trạng thái placeholder cho ô "Mật Khẩu"
    private void updateMatKhauPlaceholder(JPasswordField passwordField, boolean forcePlaceholder) {
        SwingUtilities.invokeLater(() -> {
            String password = new String(passwordField.getPassword());
            if (forcePlaceholder || password.isEmpty()) {
                passwordField.setText("VD: abc12345");
                passwordField.setForeground(Color.GRAY);
                passwordField.setCaretPosition(0);
                isMatKhauPlaceholderActive = true;
            } else if (!password.equals("VD: abc12345")) {
                passwordField.setForeground(Color.BLACK);
                isMatKhauPlaceholderActive = false;
            }
            passwordField.invalidate();
            passwordField.repaint();
        });
    }

    public Login() {
        loginController = new LoginController();

        setTitle("Thư Viện MINI - Đăng Nhập");
        setSize(350, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chính: pnl_DangNhap
        JPanel pnl_DangNhap = new JPanel();
        pnl_DangNhap.setLayout(null);
        pnl_DangNhap.setBackground(new Color(244, 231, 228));

        // Nhãn tiêu đề: lbl_Title
        JLabel lbl_Title = new JLabel("Thư Viện MINI", SwingConstants.CENTER);
        lbl_Title.setFont(new Font("Arial", Font.BOLD, 20));
        lbl_Title.setBounds(0, 20, 350, 30);
        pnl_DangNhap.add(lbl_Title);

        // Nhãn avatar: lbl_Avatar
        JLabel lbl_Avatar = new JLabel();
        
        // Đọc hình ảnh từ file
        ImageIcon avatarIcon;
        try {
            avatarIcon = new ImageIcon("Pictures/avatar.png");
            if (avatarIcon.getIconWidth() == -1) {
                throw new Exception("Hình ảnh không tải được");
            }
        } catch (Exception e) {
            lbl_Avatar.setOpaque(true);
            lbl_Avatar.setBackground(Color.GRAY);
            lbl_Avatar.setBounds(135, 60, 80, 80);
            pnl_DangNhap.add(lbl_Avatar);
            avatarIcon = null;
        }

        if (avatarIcon != null) {
            Image image = avatarIcon.getImage();
            Image scaledImage = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            avatarIcon = new ImageIcon(scaledImage);
            lbl_Avatar.setIcon(avatarIcon);
            lbl_Avatar.setBounds(135, 60, 80, 80);
            lbl_Avatar.setOpaque(false);
            pnl_DangNhap.add(lbl_Avatar);
        }

        // Nhãn "Tài Khoản": lbl_TaiKhoan
        JLabel lbl_TaiKhoan = new JLabel("Tài Khoản");
        lbl_TaiKhoan.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl_TaiKhoan.setBounds(40, 150, 100, 25);
        pnl_DangNhap.add(lbl_TaiKhoan);

        // Ô nhập "Tài Khoản": txt_TaiKhoan
        JTextField txt_TaiKhoan = new JTextField("VD: HuyenPhuThuan");
        txt_TaiKhoan.setFont(new Font("Arial", Font.ITALIC, 12));
        txt_TaiKhoan.setForeground(Color.GRAY);
        txt_TaiKhoan.setBounds(40, 180, 270, 30);
        txt_TaiKhoan.setEditable(true);
        txt_TaiKhoan.setFocusable(true);
        txt_TaiKhoan.setCaretPosition(0);
        txt_TaiKhoan.setHorizontalAlignment(JTextField.LEFT);
        pnl_DangNhap.add(txt_TaiKhoan);

        // Thêm FocusListener cho ô "Tài Khoản"
        txt_TaiKhoan.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isTaiKhoanPlaceholderActive) {
                    txt_TaiKhoan.setText("");
                    txt_TaiKhoan.setForeground(Color.BLACK);
                    txt_TaiKhoan.setCaretPosition(0);
                    isTaiKhoanPlaceholderActive = false;
                    txt_TaiKhoan.invalidate();
                    txt_TaiKhoan.repaint();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateTaiKhoanPlaceholder(txt_TaiKhoan, false);
            }
        });

        // Thêm KeyListener để xóa placeholder khi người dùng gõ ký tự đầu tiên
        txt_TaiKhoan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (isTaiKhoanPlaceholderActive) {
                    txt_TaiKhoan.setText("");
                    txt_TaiKhoan.setForeground(Color.BLACK);
                    isTaiKhoanPlaceholderActive = false;
                    txt_TaiKhoan.invalidate();
                    txt_TaiKhoan.repaint();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (isTaiKhoanPlaceholderActive) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT ||
                        e.getKeyCode() == KeyEvent.VK_HOME || e.getKeyCode() == KeyEvent.VK_END) {
                        e.consume();
                        txt_TaiKhoan.setCaretPosition(0);
                    }
                }
            }
        });

        // Thêm DocumentListener để kiểm tra khi xóa nội dung
        txt_TaiKhoan.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Không cần xử lý vì đã có KeyListener
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTaiKhoanPlaceholder(txt_TaiKhoan, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Không cần xử lý
            }
        });

        // Thêm MouseListener để chặn chọn văn bản khi placeholder hiển thị
        txt_TaiKhoan.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isTaiKhoanPlaceholderActive) {
                    txt_TaiKhoan.setCaretPosition(0);
                    txt_TaiKhoan.setSelectionStart(0);
                    txt_TaiKhoan.setSelectionEnd(0);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isTaiKhoanPlaceholderActive) {
                    txt_TaiKhoan.setCaretPosition(0);
                    txt_TaiKhoan.setSelectionStart(0);
                    txt_TaiKhoan.setSelectionEnd(0);
                }
            }
        });

        // Nhãn "Mật Khẩu": lbl_MatKhau
        JLabel lbl_MatKhau = new JLabel("Mật Khẩu");
        lbl_MatKhau.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl_MatKhau.setBounds(40, 220, 100, 25);
        pnl_DangNhap.add(lbl_MatKhau);

        // Ô nhập "Mật Khẩu": txt_MatKhau
        JPasswordField txt_MatKhau = new JPasswordField("VD: abc12345");
        txt_MatKhau.setFont(new Font("Arial", Font.ITALIC, 12));
        txt_MatKhau.setForeground(Color.GRAY);
        txt_MatKhau.setBounds(40, 250, 270, 30);
        txt_MatKhau.setEditable(true);
        txt_MatKhau.setFocusable(true);
        txt_MatKhau.setCaretPosition(0);
        pnl_DangNhap.add(txt_MatKhau);

        // Thêm FocusListener cho ô "Mật Khẩu"
        txt_MatKhau.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isMatKhauPlaceholderActive) {
                    txt_MatKhau.setText("");
                    txt_MatKhau.setForeground(Color.BLACK);
                    txt_MatKhau.setCaretPosition(0);
                    isMatKhauPlaceholderActive = false;
                    txt_MatKhau.invalidate();
                    txt_MatKhau.repaint();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateMatKhauPlaceholder(txt_MatKhau, false);
            }
        });

        // Thêm KeyListener để xóa placeholder khi người dùng gõ ký tự đầu tiên (cho ô "Mật Khẩu")
        txt_MatKhau.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (isMatKhauPlaceholderActive) {
                    txt_MatKhau.setText("");
                    txt_MatKhau.setForeground(Color.BLACK);
                    isMatKhauPlaceholderActive = false;
                    txt_MatKhau.invalidate();
                    txt_MatKhau.repaint();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (isMatKhauPlaceholderActive) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT ||
                        e.getKeyCode() == KeyEvent.VK_HOME || e.getKeyCode() == KeyEvent.VK_END) {
                        e.consume();
                        txt_MatKhau.setCaretPosition(0);
                    }
                }
            }
        });

        // Thêm DocumentListener để kiểm tra khi xóa nội dung (cho ô "Mật Khẩu")
        txt_MatKhau.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Không cần xử lý vì đã có KeyListener
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateMatKhauPlaceholder(txt_MatKhau, false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Không cần xử lý
            }
        });

        // Thêm MouseListener để chặn chọn văn bản khi placeholder hiển thị (cho ô "Mật Khẩu")
        txt_MatKhau.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isMatKhauPlaceholderActive) {
                    txt_MatKhau.setCaretPosition(0);
                    txt_MatKhau.setSelectionStart(0);
                    txt_MatKhau.setSelectionEnd(0);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isMatKhauPlaceholderActive) {
                    txt_MatKhau.setCaretPosition(0);
                    txt_MatKhau.setSelectionStart(0);
                    txt_MatKhau.setSelectionEnd(0);
                }
            }
        });

        // Nút "Đăng nhập": btn_DangNhap
        JButton btn_DangNhap = new JButton("Đăng nhập");
        btn_DangNhap.setBounds(40, 300, 270, 40);
        btn_DangNhap.setBackground(new Color(166, 107, 107));
        btn_DangNhap.setForeground(Color.WHITE);
        btn_DangNhap.setFocusPainted(false);
        pnl_DangNhap.add(btn_DangNhap);

        btn_DangNhap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lấy thông tin đăng nhập từ form
                String username = isTaiKhoanPlaceholderActive ? "" : txt_TaiKhoan.getText();
                String password = isMatKhauPlaceholderActive ? "" : new String(txt_MatKhau.getPassword());
                
                // Gọi phương thức đăng nhập từ Controller
                loginController.login(username, password, new LoginController.LoginCallBack() {
                    @Override
                    public void onSuccess(String username, boolean isAdmin) {
                        // Ẩn form đăng nhập
                        dispose();
                        
                        // Điều hướng dựa vào vai trò người dùng
                        if (isAdmin) {
                            SwingUtilities.invokeLater(() -> {
                                new Librarian(username).showUI();
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                new User(username).showUI();
                            });
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        JOptionPane.showMessageDialog(
                            Login.this, 
                            message, 
                            "Lỗi đăng nhập", 
                            JOptionPane.ERROR_MESSAGE
                        );
                    }

                    @Override
                    public void onError(String errorMessage) {
                        JOptionPane.showMessageDialog(
                            Login.this, 
                            errorMessage, 
                            "Lỗi hệ thống", 
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
            }
        });

        add(pnl_DangNhap);
        pnl_DangNhap.revalidate();
        pnl_DangNhap.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login app = new Login();
            app.setVisible(true);
        });
    }
}