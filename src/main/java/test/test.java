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
		 List<LichSuMuonSach> ds = LichSuMuonSachDao.getInstance().layDanhSach(); 
	        for (int i = 0; i < ds.size(); i++) {
	            LichSuMuonSach ls = ds.get(i);
	            System.out.println(" " + ls.getMaSach());
	        }

	}
}
