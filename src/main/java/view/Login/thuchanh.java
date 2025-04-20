import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class thuchanh extends JFrame {

    public thuchanh() {
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
            avatarIcon = new ImageIcon("avatar.png"); // Đảm bảo file avatar.png nằm trong cùng thư mục
            if (avatarIcon.getIconWidth() == -1) {
                throw new Exception("Hình ảnh không tải được");
            }
        } catch (Exception e) {
            // Nếu không tải được hình ảnh, hiển thị placeholder màu xám
            lbl_Avatar.setOpaque(true);
            lbl_Avatar.setBackground(Color.GRAY);
            lbl_Avatar.setBounds(135, 60, 80, 80);
            pnl_DangNhap.add(lbl_Avatar);
            avatarIcon = null;
        }

        if (avatarIcon != null) {
            Image image = avatarIcon.getImage();
            // Thay đổi kích thước hình ảnh thành 80x80
            Image scaledImage = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            avatarIcon = new ImageIcon(scaledImage);

            // Đặt hình ảnh vào JLabel
            lbl_Avatar.setIcon(avatarIcon);
            lbl_Avatar.setBounds(135, 60, 80, 80); // Đặt chính giữa
            lbl_Avatar.setOpaque(false); // Đảm bảo nền trong suốt
            pnl_DangNhap.add(lbl_Avatar);
        }

        // Nhãn "Tài Khoản": lbl_TaiKhoan
        JLabel lbl_TaiKhoan = new JLabel("Tài Khoản");
        lbl_TaiKhoan.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl_TaiKhoan.setBounds(40, 150, 100, 25);
        pnl_DangNhap.add(lbl_TaiKhoan);

        // Ô nhập "Tài Khoản": txt_TaiKhoan
        JTextField txt_TaiKhoan = new JTextField("VD: HuyenPhuThuan");
        txt_TaiKhoan.setFont(new Font("Arial", Font.ITALIC, 12)); // In nghiêng ví dụ
        txt_TaiKhoan.setForeground(new Color(0, 0, 0, 128)); // Làm mờ 50% (alpha = 128)
        txt_TaiKhoan.setBounds(40, 180, 270, 30);
        txt_TaiKhoan.setEditable(false);
        pnl_DangNhap.add(txt_TaiKhoan);

        // Nhãn "Mật Khẩu": lbl_MatKhau
        JLabel lbl_MatKhau = new JLabel("Mật Khẩu");
        lbl_MatKhau.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl_MatKhau.setBounds(40, 220, 100, 25);
        pnl_DangNhap.add(lbl_MatKhau);

        // Ô nhập "Mật Khẩu": txt_MatKhau
        JPasswordField txt_MatKhau = new JPasswordField("VD: abc12345");
        txt_MatKhau.setFont(new Font("Arial", Font.ITALIC, 12)); // In nghiêng ví dụ
        txt_MatKhau.setForeground(new Color(0, 0, 0, 128)); // Làm mờ 50% (alpha = 128)
        txt_MatKhau.setBounds(40, 250, 270, 30);
        txt_MatKhau.setEditable(false);
        pnl_DangNhap.add(txt_MatKhau);

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
                JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
            }
        });

        add(pnl_DangNhap);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            thuchanh app = new thuchanh();
            app.setVisible(true);
        });
    }
}