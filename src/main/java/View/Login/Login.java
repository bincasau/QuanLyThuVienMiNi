import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Login extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private final String usernamePlaceholder = "VD: HuyenPhuThuan";
    private final String passwordPlaceholder = "VD: abc12345";

    public Login() {
        setTitle("Thư Viện MINI - Đăng Nhập");
        setSize(350, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(244, 231, 228));

        // --- Tiêu đề ---
        JLabel lblTitle = new JLabel("Thư Viện MINI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(0, 20, 350, 30);
        panel.add(lblTitle);

        // --- Ảnh Avatar ---
        JLabel lblAvatar = new JLabel();
        lblAvatar.setBounds(135, 60, 80, 80);
        try {
            ImageIcon avatarIcon = new ImageIcon("Pictures/avatar.png"); // <-- ảnh ở thư mục Pictures/avatar.png
            Image img = avatarIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblAvatar.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblAvatar.setOpaque(true);
            lblAvatar.setBackground(Color.GRAY); // nếu load lỗi, nền màu xám
        }
        panel.add(lblAvatar);

        // --- Label Tài Khoản ---
        JLabel lblUsername = new JLabel("Tài Khoản");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUsername.setBounds(40, 150, 100, 25);
        panel.add(lblUsername);

        // --- TextField Username ---
        txtUsername = new JTextField(usernamePlaceholder);
        setupPlaceholder(txtUsername, usernamePlaceholder);
        txtUsername.setBounds(40, 180, 270, 30);
        panel.add(txtUsername);

        // --- Label Mật Khẩu ---
        JLabel lblPassword = new JLabel("Mật Khẩu");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPassword.setBounds(40, 220, 100, 25);
        panel.add(lblPassword);

        // --- PasswordField ---
        txtPassword = new JPasswordField(passwordPlaceholder);
        setupPlaceholder(txtPassword, passwordPlaceholder);
        txtPassword.setBounds(40, 250, 270, 30);
        panel.add(txtPassword);

        // --- Nút Đăng Nhập ---
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(40, 300, 270, 40);
        btnLogin.setBackground(new Color(166, 107, 107));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đăng nhập giả lập thành công!"));
        panel.add(btnLogin);

        // --- Thêm panel vào JFrame ---
        add(panel);

        // --- Ngăn tự động focus vào ô username lúc khởi động ---
        SwingUtilities.invokeLater(() -> {
            panel.requestFocusInWindow();
        });
    }

    private void setupPlaceholder(JTextComponent field, String placeholder) {
        field.setFont(new Font("Arial", Font.ITALIC, 12));
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setFont(new Font("Arial", Font.PLAIN, 12));
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setFont(new Font("Arial", Font.ITALIC, 12));
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
