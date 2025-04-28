package view.Librarian;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;

import java.awt.*;
import DAO.SachDao;
import Model.Sach;

public class Book extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel pnl_content;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
	         public void run() {
	              try {
	                  Book frame = new Book();
	                  frame.setVisible(true);
	              } catch (Exception e) {
	                  e.printStackTrace();
	              }
	            }
	        });
	    }
	public Book() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,600);
		setTitle("Thư viện mini");
		setVisible(true);
		setLocationRelativeTo(null);
		
		pnl_content= new JPanel(new BorderLayout());
		pnl_content.setBorder(new EmptyBorder(5,5,5,5));
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
        btn_Book.setBackground(new Color(182,162,162));
        btn_Book.setOpaque(true);
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
        pnl_Sidebar.add(btn_Logout);
        
        // Thêm xử lý sự kiện đăng xuất
        btn_Logout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                View.Login.Login loginForm = new View.Login.Login();
                loginForm.setVisible(true);
            });
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

	        JLabel lbl_LibrarianName = new JLabel("Thủ thư: Nguyễn Văn A" );
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
	 private JPanel createMain() {
		 JPanel pnl_Main = new JPanel(new BorderLayout());
		 
		 JPanel pnl_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		 pnl_top.setBackground(Color.WHITE);
		 JTextField txt_search = new JTextField(25);
		 txt_search.setPreferredSize(new Dimension(0,40));
		 txt_search.setMaximumSize(new Dimension(300,40));
		 pnl_top.add(txt_search);
		 
		 JButton btn_search = new JButton ("🔍");
		 btn_search.setPreferredSize(new Dimension(40,40));
		 btn_search.setMaximumSize(new Dimension(40,40));
		 btn_search.setMinimumSize(new Dimension(40,40));
		 pnl_top.add(btn_search);
		 
		 JButton btn_add =new JButton("Thêm");
		 btn_add.setPreferredSize(new Dimension(80,40));
		 btn_add.setMaximumSize(new Dimension(80,40));
		 btn_add.setMinimumSize(new Dimension(80,40));
		 pnl_top.add(btn_add);
		 
		 JButton btn_delete = new JButton("Xóa");
		 btn_delete.setPreferredSize(new Dimension(80,40));
		 btn_delete.setMaximumSize(new Dimension(80,40));
		 btn_delete.setMinimumSize(new Dimension(80,40));
		 pnl_top.add(btn_delete);
		 
		 //Hiển thị sách
		 JPanel pnl_center = new JPanel (new GridLayout(0,5,10,10));
		 pnl_center.setBackground(Color.WHITE);
		 pnl_center.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		 SachDao sachDao = SachDao.getInstance();
		 List <Sach> BookList = sachDao.layDanhSach(); 
		 for(Sach book : BookList) {
			 JPanel pnl_card = createCard(book);
			 pnl_center.add(pnl_card);
		 }
	     JScrollPane scrollPane = new JScrollPane(pnl_center);
	     
		 pnl_Main.add(pnl_top,BorderLayout.NORTH);
		 pnl_Main.add(scrollPane,BorderLayout.CENTER);
		 
		 return pnl_Main;
	 }
	 private JPanel createCard( Sach book) {
		 JPanel pnl_card = new JPanel();
	     pnl_card.setLayout(new BorderLayout());
	     pnl_card.setBackground(Color.WHITE);
	     pnl_card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	     pnl_card.setPreferredSize(new Dimension(120, 180));
	     
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
	 
}
