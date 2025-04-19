package DAO;

import java.util.List;

public interface InterfaceDao <T>{
	public int themDoiTuong(T t);
	public int xoaDoiTuong(T t);
	public int capNhatDoiTuong(T t);
	public List<T> layDanhSach();
	public List<T> layDanhSachTheoDK(String dk);
}
