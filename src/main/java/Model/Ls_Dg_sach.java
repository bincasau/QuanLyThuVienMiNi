package Model;

import java.sql.Date;

public class Ls_Dg_sach {
	protected String maNguoiDung;
	protected String tenNguoiDung;
	protected String maSach;
	protected String tenSach;
	protected String maLichSu;
	protected Date ngayMuon;
	protected Date ngayTra;
	protected String trangThai;
	protected String anh;
	public Ls_Dg_sach(String maNguoiDung, String tenNguoiDung, String maSach, String tenSach, String maLichSu,
			Date ngayMuon, Date ngayTra, String trangThai, String anh) {
		super();
		this.maNguoiDung = maNguoiDung;
		this.tenNguoiDung = tenNguoiDung;
		this.maSach = maSach;
		this.tenSach = tenSach;
		this.maLichSu = maLichSu;
		this.ngayMuon = ngayMuon;
		this.ngayTra = ngayTra;
		this.trangThai = trangThai;
		this.anh = anh;
	}
	public Ls_Dg_sach() {
		super();
	}
	public String getMaNguoiDung() {
		return maNguoiDung;
	}
	public void setMaNguoiDung(String maNguoiDung) {
		this.maNguoiDung = maNguoiDung;
	}
	public String getTenNguoiDung() {
		return tenNguoiDung;
	}
	public void setTenNguoiDung(String tenNguoiDung) {
		this.tenNguoiDung = tenNguoiDung;
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
	public String getMaLichSu() {
		return maLichSu;
	}
	public void setMaLichSu(String maLichSu) {
		this.maLichSu = maLichSu;
	}
	public Date getNgayMuon() {
		return ngayMuon;
	}
	public void setNgayMuon(Date ngayMuon) {
		this.ngayMuon = ngayMuon;
	}
	public Date getNgayTra() {
		return ngayTra;
	}
	public void setNgayTra(Date ngayTra) {
		this.ngayTra = ngayTra;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public String getAnh() {
		return anh;
	}
	public void setAnh(String anh) {
		this.anh = anh;
	}
	
}

