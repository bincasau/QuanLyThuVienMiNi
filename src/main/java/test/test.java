package test;

import java.util.List;

import DAO.SachDao;
import Model.Sach;

public class test {
	public static void main(String[] args) {
		List<Sach> ds = SachDao.getInstance().layDanhSach();
		for(Sach s : ds) {
			System.out.println(s);
		}
	}
}
