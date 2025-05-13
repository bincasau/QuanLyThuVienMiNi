package View.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import DAO.Ls_Dg_sachDao;
import DAO.PhieuPhatDao;
import DAO.SachDao;
import Model.Ls_Dg_sach;
import Model.PhieuPhat;
import Model.Sach;
import Model.TheLoai;
import View.Login.Login;


public class PenaltyTicket extends JFrame {

	    private static final long serialVersionUID = 1L;
	    private String maDG;
		private JPanel contentPane;
	    private String fullName;
	    private JPanel pnl_content;

	    public PenaltyTicket(String fullName,String maDG) {
	        this.fullName = fullName;
	        this.maDG=maDG;
	        initializeUI();
	    }

	    public PenaltyTicket() {
	        this.fullName = "Guest";
	        this.maDG=null;
	        initializeUI();
	    }

	    private void initializeUI() {
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setTitle("Thư viện mini");
	        setSize(1000, 600);
	        setLocationRelativeTo(null);

	        contentPane = new JPanel(new BorderLayout());
	        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	        setContentPane(contentPane);

	        JPanel pnl_Sidebar = createSidebar();
	        contentPane.add(pnl_Sidebar, BorderLayout.WEST);

	        JPanel pnl_Main = new JPanel(new BorderLayout());

	        JPanel pnl_Header = createHeader();
	        pnl_Main.add(pnl_Header, BorderLayout.NORTH);
	        
	        pnl_content = createContent();
	        pnl_Main.add(pnl_content,BorderLayout.CENTER);
	        
	        contentPane.add(pnl_Main,BorderLayout.CENTER);
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

	        String[] btnLabels = {"Trang chủ", "Lịch sử", "Phiếu phạt", "Đăng xuất"};
	        for (String text : btnLabels) {
	            JButton btn = new JButton(text);
	            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
	            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
	            btn.setHorizontalAlignment(SwingConstants.LEFT);
	            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
	            if (text.equals("Phiếu phạt")) {
	            	btn.setBackground(new Color(230,221,209));
	            	btn.setOpaque(true);
	            }
	            else if (text.equals("Đăng xuất")) {
	                btn.addActionListener(e -> {
	                    dispose();
	                    SwingUtilities.invokeLater(() -> new Login().setVisible(true));
	                });
	            }
	            pnl_ButtonGroup.add(Box.createRigidArea(new Dimension(0, 10)));
	            pnl_ButtonGroup.add(btn);
	        }

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

	        JButton btn_Avatar = createIconButton("pictures/profile.png", "👤");
	        JButton btn_Notification = createIconButton("pictures/bell.png", "🔔");

	        JLabel lbl_UserName = new JLabel(fullName);
	        lbl_UserName.setFont(new Font("SansSerif", Font.PLAIN, 14));
	        lbl_UserName.setForeground(Color.WHITE);

	        pnl_TopRow.add(btn_Avatar);
	        pnl_TopRow.add(Box.createHorizontalStrut(10));
	        pnl_TopRow.add(lbl_UserName);
	        pnl_TopRow.add(Box.createHorizontalGlue());
	        pnl_TopRow.add(btn_Notification);
	        pnl_Header.add(pnl_TopRow, BorderLayout.NORTH);
	        return pnl_Header;
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
	    
	    private JPanel createContent() {
		    JPanel pnl_content = new JPanel();
		    pnl_content.setLayout(new BoxLayout(pnl_content, BoxLayout.Y_AXIS));
		    pnl_content.setBorder(new EmptyBorder(10, 20, 10, 20));
		    pnl_content.setBackground(Color.WHITE);
		
		    if (this.maDG == null || this.maDG.isEmpty()) {
		        JLabel lbl_warning = new JLabel("Vui lòng đăng nhập để sử dụng tính năng này");
		        lbl_warning.setFont(new Font("SansSerif", Font.BOLD, 20));
		        lbl_warning.setForeground(Color.RED);
		        lbl_warning.setHorizontalAlignment(SwingConstants.CENTER);
		        pnl_content.add(lbl_warning, BorderLayout.CENTER);
		        return pnl_content;
		    }
		    
		    PhieuPhatDao phieuPhatDao = new PhieuPhatDao();
		    List<PhieuPhat> dsPhieuPhat = phieuPhatDao.layDanhSachTheoDK(maDG);  // Lấy toàn bộ danh sách phiếu phạt
		
		    SachDao sachDao = SachDao.getInstance();
		
		    for (PhieuPhat phieu : dsPhieuPhat) {
		        // Lấy tên sách từ maSach
		        List<Sach> dsSach = sachDao.layDanhSachTheoDK(phieu.getMaSach());
		        String tenSach = dsSach.isEmpty() ? "Không tìm thấy tên sách" : dsSach.get(0).getTenSach();
		
		        // Panel chứa từng phiếu phạt
		        JPanel pnl_item = new JPanel(new BorderLayout());
		        pnl_item.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		        pnl_item.setBackground(Color.WHITE);
		        pnl_item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		        
		        // Tên sách
		        JLabel lbl_tenSach = new JLabel(tenSach);
		        lbl_tenSach.setFont(new Font("SansSerif", Font.BOLD, 16));
		        pnl_item.add(lbl_tenSach, BorderLayout.NORTH);
		
		        // Ngày tạo phiếu
		        JLabel lbl_ngay = new JLabel("Date: " + phieu.getNgayPhieu());
		        lbl_ngay.setFont(new Font("SansSerif", Font.PLAIN, 12));
		        lbl_ngay.setForeground(Color.GRAY);
		        pnl_item.add(lbl_ngay, BorderLayout.CENTER);
		
		        // Trạng thái
		        JLabel lbl_trangThai = new JLabel();
		        lbl_trangThai.setFont(new Font("SansSerif", Font.PLAIN, 14));
		        if (phieu.getLoi().equals("Trả sách trễ")) {
		            lbl_trangThai.setText("Quá hạn");
		            lbl_trangThai.setForeground(Color.RED);
		        } else {
		            lbl_trangThai.setText("Làm mất sách");
		            lbl_trangThai.setForeground(Color.RED);
		        }
		        pnl_item.add(lbl_trangThai, BorderLayout.EAST);
		
		        // Thêm sự kiện nhấn cho toàn bộ panel
		        pnl_item.addMouseListener(new MouseAdapter() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
		                // Mở panel chi tiết
		                pnl_content.removeAll();
		                pnl_content.add(createDetailPanel(phieu.getMaSach(),phieu.getLoi(),phieu.getGiaTien()));
		                pnl_content.revalidate();
		                pnl_content.repaint();
		            }
		
		            @Override
		            public void mouseEntered(MouseEvent e) {
		                pnl_item.setBackground(new Color(245, 245, 245));  // Đổi màu khi hover
		                pnl_item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		            }
		
		            @Override
		            public void mouseExited(MouseEvent e) {
		                pnl_item.setBackground(Color.WHITE);  // Trả lại màu ban đầu
		                pnl_item.setCursor(Cursor.getDefaultCursor());
		            }
		        });
		
		        // Thêm vào panel chính
		        pnl_content.add(pnl_item);
		        pnl_content.add(Box.createVerticalStrut(10));  // Khoảng cách giữa các phiếu phạt
		    }
		
		    return pnl_content;
		}
	    
	    private JPanel createDetailPanel(String maSach, String loi, double tienPhat) {
	        JPanel pnl_detail = new JPanel(new BorderLayout(10, 10));
	        pnl_detail.setBorder(new EmptyBorder(10, 10, 10, 10));
	        pnl_detail.setBackground(Color.WHITE);

	        // Thêm thanh cuộn cho panel chi tiết
	        JScrollPane scrollPane = new JScrollPane();
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	        scrollPane.setBorder(BorderFactory.createEmptyBorder());

	        Ls_Dg_sachDao lsdgsachDao = new Ls_Dg_sachDao().getInstance();
	        List<Ls_Dg_sach> ds = lsdgsachDao.layDanhSachTheoMaDocGiaVaMaSach(maDG, maSach);
	        
	        if (ds.isEmpty()) {
	            JLabel lbl_empty = new JLabel("Không tìm thấy dữ liệu.");
	            lbl_empty.setFont(new Font("SansSerif", Font.BOLD, 20));
	            lbl_empty.setForeground(Color.GRAY);
	            pnl_detail.add(lbl_empty, BorderLayout.CENTER);
	            return pnl_detail;
	        }
	        
	        Ls_Dg_sach chiTiet = ds.get(0);
	        
	        // Panel chính chia đôi (Hình và thông tin)
	        JPanel pnl_mainContainer = new JPanel();
	        pnl_mainContainer.setLayout(new GridBagLayout());
	        pnl_mainContainer.setBackground(Color.WHITE);

	        // Phần ảnh sách + Tên sách + Tags (bên trái)
	        JPanel pnl_left = new JPanel();
	        pnl_left.setLayout(new BoxLayout(pnl_left, BoxLayout.Y_AXIS));
	        pnl_left.setBackground(Color.WHITE);

	        JLabel lbl_image = new JLabel();
	        if (maSach != null && !maSach.isEmpty()) {
	            ImageIcon imageIcon = new ImageIcon("Pictures/" + maSach + ".jpg");
	            Image image = imageIcon.getImage().getScaledInstance(180, 240, Image.SCALE_SMOOTH);
	            lbl_image.setIcon(new ImageIcon(image));
	        }
	        lbl_image.setAlignmentX(Component.CENTER_ALIGNMENT);
	        pnl_left.add(lbl_image);
	        pnl_left.add(Box.createVerticalStrut(10));

	        JLabel lbl_title = new JLabel(chiTiet.getTenSach());
	        lbl_title.setFont(new Font("SansSerif", Font.BOLD, 28));
	        lbl_title.setAlignmentX(Component.CENTER_ALIGNMENT);
	        pnl_left.add(lbl_title);
	        pnl_left.add(Box.createVerticalStrut(5));

	        JPanel pnl_tags = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
	        pnl_tags.setBackground(Color.WHITE);
	        SachDao sachDao = SachDao.getInstance();
	        List<Sach> dsSach = sachDao.layDanhSachTheoDK(maSach);
	        if (!dsSach.isEmpty()) {
	            Sach sach = dsSach.get(0);
	            for (TheLoai theLoai : sach.getDsTheLoai()) {
	                pnl_tags.add(createTag(theLoai.getTenTheLoai()));
	            }
	        }
	        pnl_tags.setAlignmentX(Component.CENTER_ALIGNMENT);
	        pnl_left.add(pnl_tags);

	        // Gói pnl_left để cố định vị trí bên trái
	        GridBagConstraints gbc_left = new GridBagConstraints();
	        gbc_left.gridx = 0;
	        gbc_left.gridy = 0;
	        gbc_left.anchor = GridBagConstraints.NORTHWEST;
	        gbc_left.insets = new Insets(0, 0, 0, 20); // Khoảng cách giữa ảnh và thông tin
	        pnl_mainContainer.add(pnl_left, gbc_left);

	        // Phần thông tin sách (bên phải, chỉ căn giữa theo chiều dọc)
	        JPanel pnl_info = new JPanel();
	        pnl_info.setLayout(new BoxLayout(pnl_info, BoxLayout.Y_AXIS));
	        pnl_info.setBackground(Color.WHITE);

	        // Ngày mượn, ngày trả, lỗi và số tiền phạt
	        JLabel lbl_ngayMuon = new JLabel("Ngày mượn: " + chiTiet.getNgayMuon());
	        lbl_ngayMuon.setFont(new Font("SansSerif", Font.PLAIN, 16));
	        pnl_info.add(lbl_ngayMuon);

	        JLabel lbl_ngayTra = new JLabel("Ngày trả: " + chiTiet.getNgayTra());
	        lbl_ngayTra.setFont(new Font("SansSerif", Font.PLAIN, 16));
	        pnl_info.add(lbl_ngayTra);
	        
	        pnl_info.add(Box.createVerticalStrut(5));

	        JLabel lbl_loi = new JLabel("Lỗi: " + loi);
	        lbl_loi.setFont(new Font("SansSerif", Font.BOLD, 18));
	        lbl_loi.setForeground(Color.RED);
	        pnl_info.add(lbl_loi);

	        JLabel lbl_tienPhat = new JLabel("Số tiền phạt: " + tienPhat + " VND");
	        lbl_tienPhat.setFont(new Font("SansSerif", Font.PLAIN, 16));
	        pnl_info.add(lbl_tienPhat);

	        // Gói pnl_info để căn giữa theo chiều dọc
	        GridBagConstraints gbc_right = new GridBagConstraints();
	        gbc_right.gridx = 1;
	        gbc_right.gridy = 0;
	        gbc_right.anchor = GridBagConstraints.CENTER;
	        pnl_mainContainer.add(pnl_info, gbc_right);

	        scrollPane.setViewportView(pnl_mainContainer);
	        pnl_detail.add(scrollPane, BorderLayout.CENTER);

	        // Nút quay lại (góc dưới cùng bên phải)
	        JPanel pnl_footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        pnl_footer.setBackground(Color.WHITE);
	        JButton btn_back = new JButton("Quay lại");
	        btn_back.setPreferredSize(new Dimension(150, 40));
	        btn_back.addActionListener(e -> {
	            pnl_content.removeAll();
	            pnl_content.add(createContent());
	            pnl_content.revalidate();
	            pnl_content.repaint();
	        });
	        pnl_footer.add(btn_back);

	        pnl_detail.add(pnl_footer, BorderLayout.SOUTH);

	        return pnl_detail;
	    }
	    
	    private JLabel createTag(String tagName) {
	        JLabel lbl_tag = new JLabel(tagName);
	        lbl_tag.setFont(new Font("SansSerif", Font.PLAIN, 14));
	        lbl_tag.setOpaque(true);
	        lbl_tag.setBackground(Color.WHITE);
	        lbl_tag.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        lbl_tag.setHorizontalAlignment(SwingConstants.CENTER);
	        lbl_tag.setPreferredSize(new Dimension(80, 30));
	        return lbl_tag;
	    }


	    public static void main(String[] args) {
	        EventQueue.invokeLater(() -> {
	            try {
	                PenaltyTicket frame = new PenaltyTicket("Nguyễn Văn A", "DG158");
	                frame.setVisible(true);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
	    }
	}
