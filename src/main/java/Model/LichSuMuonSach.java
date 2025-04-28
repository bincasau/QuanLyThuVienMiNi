package Model;

import java.sql.Date;

public class LichSuMuonSach {
	private String maLichSu;
	private Date ngayMuon;
	private Date ngayTra;
	private String trangThai;
	private String maSach;
	private String maThuThu;
	private String maDocGia;
	public LichSuMuonSach() {
		super();
	}
	public LichSuMuonSach(String maLichSu, Date ngayMuon, Date ngayTra, String trangThai, String maSach,
			String maThuThu, String maDocGia) {
		super();
		this.maLichSu = maLichSu;
		this.ngayMuon = ngayMuon;
		this.ngayTra = ngayTra;
		this.trangThai = trangThai;
		this.maSach = maSach;
		this.maThuThu = maThuThu;
		this.maDocGia = maDocGia;
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
	public String getMaSach() {
		return maSach;
	}
	public void setMaSach(String maSach) {
		this.maSach = maSach;
	}
	public String getMaThuThu() {
		return maThuThu;
	}
	public void setMaThuThu(String maThuThu) {
		this.maThuThu = maThuThu;
	}
	public String getMaDocGia() {
		return maDocGia;
	}
	public void setMaDocGia(String maDocGia) {
		this.maDocGia = maDocGia;
	}
	@Override
	public String toString() {
		return "LichSuMuonSach [maLichSu=" + maLichSu + ", ngayMuon=" + ngayMuon + ", ngayTra=" + ngayTra
				+ ", trangThai=" + trangThai + ", maSach=" + maSach + ", maThuThu=" + maThuThu + ", maDocGia="
				+ maDocGia + "]";
	}
	
	
}
