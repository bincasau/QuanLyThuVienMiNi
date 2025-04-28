package View.Librarian;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Window;


import View.Login.Login;

public class Librarian {
    private String username;
    private JFrame frame;

    // Constructor mới nhận username
    public Librarian(String username) {
        this.username = username;
    }
    
    // Constructor không tham số (để giữ tương thích với code cũ)
    public Librarian() {
        this.username = "admin";
    }

	public void showUI() {
		JFrame frame = new JFrame("Thư viện mini");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 600);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

        JPanel pnl_Sidebar = createSidebar();
        frame.add(pnl_Sidebar, BorderLayout.WEST);

		JPanel pnl_Main = new JPanel();
		pnl_Main.setBackground(Color.WHITE);
		frame.add(pnl_Main);

        JPanel pnl_Header = createHeader();
        frame.add(pnl_Header, BorderLayout.NORTH);
        
        frame.setVisible(true);
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
        pnl_Sidebar.add(btn_Book);

		JButton btn_User = new JButton("Độc giả");
		btn_User.setPreferredSize(new Dimension(200, 50));
		btn_User.setMaximumSize(new Dimension(200, 50));
		btn_User.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pnl_Sidebar.add(btn_User);

        JButton btn_Borrow = new JButton("Mượn sách");
        btn_Borrow.setPreferredSize(new Dimension(200, 50));
        btn_Borrow.setMaximumSize(new Dimension(200, 50));
        btn_Borrow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_Sidebar.add(btn_Borrow);

        JButton btn_PenaltyTicketInfo = new JButton("Thông tin phiếu phạt");
        btn_PenaltyTicketInfo.setPreferredSize(new Dimension(200, 50));
        btn_PenaltyTicketInfo.setMaximumSize(new Dimension(200, 50));
        btn_PenaltyTicketInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_Sidebar.add(btn_PenaltyTicketInfo);

        JButton btn_Statistics = new JButton("Thống kê");
        btn_Statistics.setPreferredSize(new Dimension(200, 50));
        btn_Statistics.setMaximumSize(new Dimension(200, 50));
        btn_Statistics.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_Sidebar.add(btn_Statistics);

        pnl_Sidebar.add(Box.createVerticalGlue());

        JButton btn_Logout = new JButton("Đăng xuất");
        btn_Logout.setPreferredSize(new Dimension(200, 50));
        btn_Logout.setMaximumSize(new Dimension(200, 50));
        btn_Logout.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn_Logout.setFocusable(false);
        pnl_Sidebar.add(btn_Logout);
        
        // Thêm xử lý sự kiện đăng xuất
        btn_Logout.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(btn_Logout);
            int confirm = JOptionPane.showConfirmDialog(window, 
                "Bạn có chắc chắn muốn đăng xuất?", 
                "Xác nhận đăng xuất", 
                JOptionPane.YES_NO_OPTION);
        
            if (confirm == JOptionPane.YES_OPTION) {
                // Lấy ra cái Window chứa nút btn_Logout rồi đóng nó
                
                if (window != null) {
                    window.dispose();
                }
                
                // Gọi lại giao diện Login
                View.Login.Login loginForm = new View.Login.Login();
                loginForm.setVisible(true);
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
        // Kiểm tra xem hình ảnh có tồn tại không
        if (icon_Book.getIconWidth() == -1) {
            // Nếu không tải được ảnh, tạo JLabel đơn giản
            JLabel lbl_icon = new JLabel("BOOK");
            lbl_icon.setFont(new Font("Arial", Font.BOLD, 20));
            lbl_icon.setForeground(Color.WHITE);
            lbl_icon.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 60));
            pnl_Header.add(lbl_icon);
        } else {
            // Nếu tải được ảnh, resize và hiển thị
            Image scaledImage = icon_Book.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            icon_Book = new ImageIcon(scaledImage);
            JLabel lbl_icon = new JLabel(icon_Book);
            lbl_icon.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 60));
            pnl_Header.add(lbl_icon);
        }

        JLabel lbl_Title = new JLabel("NHÀ SÁCH MINI");
        lbl_Title.setFont(new Font("Arial", Font.BOLD, 24));
        lbl_Title.setBackground(new Color(106, 85, 85));
        lbl_Title.setBorder(null);
        pnl_Header.add(lbl_Title);
        pnl_Header.add(Box.createHorizontalGlue());

        // Sử dụng username đã truyền vào
        JLabel lbl_LibrarianName = new JLabel("Thủ thư: " + username);
        lbl_LibrarianName.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl_LibrarianName.setForeground(Color.WHITE);
        lbl_LibrarianName.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        pnl_Header.add(lbl_LibrarianName);

        Dimension size = new Dimension(50, 50);

        // Thêm nút thông báo
        ImageIcon icon_Bell = new ImageIcon("Pictures/bell.png");
        if (icon_Bell.getIconWidth() != -1) {
            Image scaledImage = icon_Bell.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            icon_Bell = new ImageIcon(scaledImage);
            JButton btn_Notification = new JButton(icon_Bell);
            btn_Notification.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
            btn_Notification.setPreferredSize(size);
            btn_Notification.setMaximumSize(size);
            btn_Notification.setMinimumSize(size);
            pnl_Header.add(btn_Notification);
        } else {
            JButton btn_Notification = new JButton("🔔");
            btn_Notification.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
            btn_Notification.setPreferredSize(size);
            btn_Notification.setMaximumSize(size);
            btn_Notification.setMinimumSize(size);
            pnl_Header.add(btn_Notification);
        }

        // Thêm nút hồ sơ
        ImageIcon icon_Profile = new ImageIcon("Pictures/profile.png");
        if (icon_Profile.getIconWidth() != -1) {
            Image scaledImage = icon_Profile.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            icon_Profile = new ImageIcon(scaledImage);
            JButton btn_Profile = new JButton(icon_Profile);
            btn_Profile.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
            btn_Profile.setPreferredSize(size);
            btn_Profile.setMaximumSize(size);
            btn_Profile.setMinimumSize(size);
            pnl_Header.add(btn_Profile);
        } else {
            JButton btn_Profile = new JButton("👤");
            btn_Profile.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
            btn_Profile.setPreferredSize(size);
            btn_Profile.setMaximumSize(size);
            btn_Profile.setMinimumSize(size);
            pnl_Header.add(btn_Profile);
        }

		return pnl_Header;
	}

}