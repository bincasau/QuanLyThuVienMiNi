package Model;

import java.util.ArrayList;

public class Sach {
	private String maSach;
	private String tenSach;
	private String nXB;
	private int namXB;
	private double gia;
	private String anh;
	private ArrayList<TheLoai> dsTheLoai = new ArrayList<TheLoai>();

	public Sach() {
		super();
	}

	public Sach(String maSach, String tenSach, String nXB, int namXB, double gia, String anh) {
		super();
		this.maSach = maSach;
		this.tenSach = tenSach;
		this.nXB = nXB;
		this.namXB = namXB;
		this.gia = gia;
		this.anh = anh;
	}

	public String getMaSach() {
		return maSach;
	}

	public void setMaSach(String maSach) {
		this.maSach = maSach;
	}

	public String getTenSach() {
		return tenSach;
	}

	public void setTenSach(String tenSach) {
		this.tenSach = tenSach;
	}

	public String getnXB() {
		return nXB;
	}

	public void setnXB(String nXB) {
		this.nXB = nXB;
	}

	public int getNamXB() {
		return namXB;
	}

	public void setNamXB(int namXB) {
		this.namXB = namXB;
	}

	public double getGia() {
		return gia;
	}

	public void setGia(double gia) {
		this.gia = gia;
	}

	public String getAnh() {
		return anh;
	}

	public void setAnh(String anh) {
		this.anh = anh;
	}

}
