package view.User;

import javax.swing.*;
import java.awt.*;

public class User {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new User().showUI();
        });
    }

    private void showUI() {
        JFrame frame = new JFrame("Thư viện mini");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        frame.add(sidebar, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel header = createHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        mainPanel.add(content, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();

        // khung
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 600));
        sidebar.setBackground(new Color(240, 233, 222));

        // nhãn
        JLabel title = new JLabel("Thư viện MINI");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebar.add(title);

        // Panel con để chứa các nút và tạo khoảng cách với biên
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(240, 233, 222)); // Cùng màu với sidebar
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Tạo khoảng cách 10px từ biên trái và phải
        
        // nút
        String[] labels = {"Trang chủ", "Lịch sử", "Phiếu phạt", "Đăng xuất"};
        for (String label : labels) {
            RoundedButton button = new RoundedButton(label, 20);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setHorizontalAlignment(SwingConstants.LEFT);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            buttonPanel.add(button);
        }

        sidebar.add(buttonPanel);
        
        return sidebar;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 160));
        header.setBackground(new Color(107, 142, 35));
    
        JPanel topRow = new JPanel();
        topRow.setLayout(new BoxLayout(topRow, BoxLayout.X_AXIS));
        topRow.setOpaque(false);
        topRow.setBorder(BorderFactory.createEmptyBorder(10, 35, 0, 20)); 
    
        JButton avatarButton = new JButton("AVT");
        JButton notificationButton = new JButton("Noti");
        
        avatarButton.setFont(new Font("SansSerif", Font.PLAIN, 10));
        avatarButton.setFocusable(false);
        notificationButton.setFont(new Font("SansSerif", Font.PLAIN, 10));
        notificationButton.setFocusable(false);

        avatarButton.setMinimumSize(new Dimension(60, 60));
        avatarButton.setMaximumSize(new Dimension(60, 60));
        avatarButton.setPreferredSize(new Dimension(60, 60));

        notificationButton.setMinimumSize(new Dimension(60, 60));
        notificationButton.setMaximumSize(new Dimension(60, 60));
        notificationButton.setPreferredSize(new Dimension(60, 60));

        avatarButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        notificationButton.setAlignmentY(Component.CENTER_ALIGNMENT);
    
        topRow.add(avatarButton);
        topRow.add(Box.createHorizontalGlue());
        topRow.add(notificationButton);
    
        RoundedTextField searchField = new RoundedTextField(10);
        //searchField.setBorder(BorderFactory.createEmptyBorder(10, 35, 0, 20));
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setMaximumSize(new Dimension(500, 40));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setText("Tìm kiếm...");
    
        JPanel searchWrapper = new JPanel();
        searchWrapper.setOpaque(false);
        searchWrapper.setLayout(new BoxLayout(searchWrapper, BoxLayout.X_AXIS));
        searchWrapper.setBorder(BorderFactory.createEmptyBorder(15, 35, 10, 10));
        searchWrapper.add(searchField);
    
        header.add(topRow, BorderLayout.NORTH);
        header.add(searchWrapper, BorderLayout.CENTER);
    
        return header;
    }
    
}