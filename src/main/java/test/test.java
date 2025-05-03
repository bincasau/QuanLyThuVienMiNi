package test;

import java.util.List;

import javax.swing.Box;

import DAO.DocGiaDao;
import DAO.LichSuMuonSachDao;
import DAO.SachDao;
import Model.DocGia;
import Model.LichSuMuonSach;
import Model.Sach;

public class test {
	public static void main(String[] args) {
		List<Sach> bookList = SachDao.getInstance().layDanhSach();
		for(Sach s : bookList) {
			System.out.println(s);
		}
	}
}
