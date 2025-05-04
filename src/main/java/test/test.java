package test;

import java.util.List;

import javax.swing.Box;

import DAO.DocGiaDao;
import DAO.LichSuMuonSachDao;
import DAO.SachDao;
import DAO.ThongTinThongKeDao;
import Model.DocGia;
import Model.LichSuMuonSach;
import Model.Sach;
import Model.ThongTinThongKe;

public class test {
	public static void main(String[] args) {
		List<ThongTinThongKe> ds = ThongTinThongKeDao.getInstance().layDanhSachTheoNam(2023);
		for(ThongTinThongKe tttk : ds) {
			System.out.println(tttk);
		}
	}
}
