package view.User;

import javax.swing.*;
import java.awt.*;

public class User {
	private String username;
    private JFrame frame;

    // Constructor mới nhận username
    public User(String username) {
        this.username = username;
    }

	public User() {
        this.username = "Người dùng";
    }
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new User().showUI();
		});
	}

	public void showUI() {
		JFrame frame = new JFrame("Thư viện mini");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 600);
		frame.setLayout(new BorderLayout());

		JPanel pnl_Sidebar = createSidebar();
		frame.add(pnl_Sidebar, BorderLayout.WEST);

		JPanel pnl_Main = new JPanel(new BorderLayout());

		JPanel pnl_Header = createHeader();
		pnl_Main.add(pnl_Header, BorderLayout.NORTH);

		JPanel pnl_Content = new JPanel();
		pnl_Content.setBackground(Color.WHITE);
		pnl_Main.add(pnl_Content, BorderLayout.CENTER);

		frame.add(pnl_Main, BorderLayout.CENTER);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private JPanel createSidebar() {
		JPanel pnl_Sidebar = new JPanel();

		pnl_Sidebar.setLayout(new BoxLayout(pnl_Sidebar, BoxLayout.Y_AXIS));
		pnl_Sidebar.setPreferredSize(new Dimension(220, 600));
		pnl_Sidebar.setBackground(new Color(240, 233, 222));

		JLabel lbl_Title = new JLabel("Thư viện MINI");
		lbl_Title.setFont(new Font("SansSerif", Font.BOLD, 24));
		lbl_Title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pnl_Sidebar.add(lbl_Title);

		JPanel pnl_ButtonGroup = new JPanel();
		pnl_ButtonGroup.setLayout(new BoxLayout(pnl_ButtonGroup, BoxLayout.Y_AXIS));
		pnl_ButtonGroup.setBackground(new Color(240, 233, 222));
		pnl_ButtonGroup.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		RoundedButton btn_Home = new RoundedButton("Trang chủ", 20);
		btn_Home.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		btn_Home.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		btn_Home.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn_Home.setHorizontalAlignment(SwingConstants.LEFT);
		pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
		pnl_ButtonGroup.add(btn_Home);

		RoundedButton btn_History = new RoundedButton("Lịch sử", 20);
		btn_History.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		btn_History.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		btn_History.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn_History.setHorizontalAlignment(SwingConstants.LEFT);
		pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
		pnl_ButtonGroup.add(btn_History);

		RoundedButton btn_Penalty = new RoundedButton("Phiếu phạt", 20);
		btn_Penalty.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		btn_Penalty.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		btn_Penalty.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn_Penalty.setHorizontalAlignment(SwingConstants.LEFT);
		pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
		pnl_ButtonGroup.add(btn_Penalty);

		RoundedButton btn_Logout = new RoundedButton("Đăng xuất", 20);
		btn_Logout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		btn_Logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		btn_Logout.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn_Logout.setHorizontalAlignment(SwingConstants.LEFT);
		pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
		pnl_ButtonGroup.add(btn_Logout);

		pnl_Sidebar.add(pnl_ButtonGroup);

		return pnl_Sidebar;
	}

	private JPanel createHeader() {
		JPanel pnl_Header = new JPanel(new BorderLayout());
		pnl_Header.setPreferredSize(new Dimension(0, 160));
		pnl_Header.setBackground(new Color(107, 142, 35));

		JPanel pnl_TopRow = new JPanel();
		pnl_TopRow.setLayout(new BoxLayout(pnl_TopRow, BoxLayout.X_AXIS));
		pnl_TopRow.setOpaque(false);
		pnl_TopRow.setBorder(BorderFactory.createEmptyBorder(10, 35, 0, 20));

		JButton btn_Avatar = new JButton("AVT");
		JLabel lbl_UserName = new JLabel("Nguyễn Văn A"); // Tên khách hàng
		lbl_UserName.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lbl_UserName.setForeground(Color.WHITE);

		JButton btn_Notification = new JButton("Noti");

		btn_Avatar.setFont(new Font("SansSerif", Font.PLAIN, 10));
		btn_Avatar.setFocusable(false);
		btn_Avatar.setPreferredSize(new Dimension(60, 60));
		btn_Avatar.setMaximumSize(new Dimension(60, 60));

		btn_Notification.setFont(new Font("SansSerif", Font.PLAIN, 10));
		btn_Notification.setFocusable(false);
		btn_Notification.setPreferredSize(new Dimension(60, 60));
		btn_Notification.setMaximumSize(new Dimension(60, 60));

		btn_Avatar.setAlignmentY(Component.CENTER_ALIGNMENT);
		btn_Notification.setAlignmentY(Component.CENTER_ALIGNMENT);
		lbl_UserName.setAlignmentY(Component.CENTER_ALIGNMENT);

		pnl_TopRow.add(btn_Avatar);
		pnl_TopRow.add(Box.createHorizontalStrut(10)); // Khoảng cách giữa avatar và tên
		pnl_TopRow.add(lbl_UserName);
		pnl_TopRow.add(Box.createHorizontalGlue());
		pnl_TopRow.add(btn_Notification);

		// Create a panel to hold both the search field and search icon button
		JPanel pnl_SearchWithIcon = new JPanel();
		pnl_SearchWithIcon.setLayout(new BoxLayout(pnl_SearchWithIcon, BoxLayout.X_AXIS));
		pnl_SearchWithIcon.setOpaque(false);

		RoundedTextField txt_Search = new RoundedTextField(10);
		txt_Search.setPreferredSize(new Dimension(300, 40));
		txt_Search.setMaximumSize(new Dimension(500, 40));
		txt_Search.setFont(new Font("SansSerif", Font.PLAIN, 14));
		txt_Search.setText("Tìm kiếm...");

		// Create a search icon button
		ImageIcon searchIcon = new ImageIcon("src/main/resources/icons/search.png");
		Image scaledImage = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		searchIcon = new ImageIcon(scaledImage);

		RoundedButton btn_SearchIcon = new RoundedButton("🔍", 20);
		btn_SearchIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));
		btn_SearchIcon.setFocusPainted(false);
		btn_SearchIcon.setContentAreaFilled(true);
		btn_SearchIcon.setBorderPainted(true);
		btn_SearchIcon.setPreferredSize(new Dimension(40, 40));
		btn_SearchIcon.setMaximumSize(new Dimension(40, 40));
		btn_SearchIcon.setMinimumSize(new Dimension(40, 40));

		// Đảm bảo nút có hình dạng vuông và dễ nhìn
		btn_SearchIcon.setBackground(new Color(240, 240, 240));
		btn_SearchIcon.setOpaque(true);
		btn_SearchIcon.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		btn_SearchIcon.setMargin(new Insets(0, 0, 0, 0));

		btn_SearchIcon.setHorizontalAlignment(SwingConstants.CENTER);
		btn_SearchIcon.setVerticalAlignment(SwingConstants.CENTER);

		//////////////////////////////////////////////////////////////////////////
		// btn_SearchIcon.addActionListener(e -> { //
		// //
		// System.out.println("Searching for: " + txt_Search.getText()); //
		// }); //
		//////////////////////////////////////////////////////////////////////////

		pnl_SearchWithIcon.add(txt_Search);
		pnl_SearchWithIcon.add(Box.createRigidArea(new Dimension(5, 0)));
		pnl_SearchWithIcon.add(btn_SearchIcon);

		JPanel pnl_SearchWrapper = new JPanel();
		pnl_SearchWrapper.setOpaque(false);
		pnl_SearchWrapper.setLayout(new BoxLayout(pnl_SearchWrapper, BoxLayout.X_AXIS));
		pnl_SearchWrapper.setBorder(BorderFactory.createEmptyBorder(15, 35, 10, 10));
		pnl_SearchWrapper.add(pnl_SearchWithIcon);

		pnl_SearchWrapper.add(Box.createHorizontalGlue());

		pnl_Header.add(pnl_TopRow, BorderLayout.NORTH);
		pnl_Header.add(pnl_SearchWrapper, BorderLayout.CENTER);

		return pnl_Header;
	}

}
