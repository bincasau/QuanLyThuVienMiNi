package Controller;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import DAO.DocGiaDao;
import Model.DocGia;

public class ReaderController {
    private DocGiaDao docGiaDao = new DocGiaDao(); // gọi DAO
    private DefaultTableModel model;
    private JTable table;

    public JPanel getReaderPanel() {
        JPanel pnl_Reader = new JPanel(new BorderLayout());

        // Cột bảng
        String[] columnNames = {"Mã Độc Giả", "Tên Độc Giả", "Email", "Số Điện Thoại", "Ngày Tạo"};

        // Model bảng
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        pnl_Reader.add(scrollPane, BorderLayout.CENTER);

        // Nạp dữ liệu
        loadData();

        return pnl_Reader;
    }

    private void loadData() {
        List<DocGia> danhSach = docGiaDao.layDanhSach();
        model.setRowCount(0); // clear bảng trước
        for (DocGia docGia : danhSach) {
            model.addRow(new Object[]{
                docGia.getMaNguoiDung(),
                docGia.getTenNguoiDung(),
                docGia.getEmail(),
                docGia.getSoDienThoai(),
                docGia.getNgayTao()
            });
        }
    }
}
