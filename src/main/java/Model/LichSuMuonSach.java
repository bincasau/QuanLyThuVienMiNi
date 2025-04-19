package Model;

import java.sql.Date;

public class LichSuMuonSach {
	private String maLichSu;
	private Date ngayMuon;
	private Date ngayTra;
	private String trangThai;
	private String maDocGia;
	private String maThuThu;
	private String maSach;
	public LichSuMuonSach() {
		super();
	}
	public LichSuMuonSach(String maLichSu, Date ngayMuon, Date ngayTra, String trangThai, String maDocGia,
			String maThuThu, String maSach) {
		super();
		this.maLichSu = maLichSu;
		this.ngayMuon = ngayMuon;
		this.ngayTra = ngayTra;
		this.trangThai = trangThai;
		this.maDocGia = maDocGia;
		this.maThuThu = maThuThu;
		this.maSach = maSach;
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
	public String getMaDocGia() {
		return maDocGia;
	}
	public void setMaDocGia(String maDocGia) {
		this.maDocGia = maDocGia;
	}
	public String getMaThuThu() {
		return maThuThu;
	}
	public void setMaThuThu(String maThuThu) {
		this.maThuThu = maThuThu;
	}
	public String getMaSach() {
		return maSach;
	}
	public void setMaSach(String maSach) {
		this.maSach = maSach;
	}
	
}
