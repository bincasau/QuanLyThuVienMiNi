package Model;

public class ThongTinThongKe {
	private int thang;
	private int soDocGiaMoi;
	private int soLuongTraSachTre;
	private int soLuongMatSach;
	private int soLuongLamHuSach;
	private double doanhThu;
	public ThongTinThongKe() {
		super();
	}
	public ThongTinThongKe(int thang, int soDocGiaMoi, int soLuongTraSachTre, int soLuongMatSach, int soLuongLamHuSach,
			double doanhThu) {
		super();
		this.thang = thang;
		this.soDocGiaMoi = soDocGiaMoi;
		this.soLuongTraSachTre = soLuongTraSachTre;
		this.soLuongMatSach = soLuongMatSach;
		this.soLuongLamHuSach = soLuongLamHuSach;
		this.doanhThu = doanhThu;
	}
	public int getThang() {
		return thang;
	}
	public void setThang(int thang) {
		this.thang = thang;
	}
	public int getSoDocGiaMoi() {
		return soDocGiaMoi;
	}
	public void setSoDocGiaMoi(int soDocGiaMoi) {
		this.soDocGiaMoi = soDocGiaMoi;
	}
	public int getSoLuongTraSachTre() {
		return soLuongTraSachTre;
	}
	public void setSoLuongTraSachTre(int soLuongTraSachTre) {
		this.soLuongTraSachTre = soLuongTraSachTre;
	}
	public int getSoLuongMatSach() {
		return soLuongMatSach;
	}
	public void setSoLuongMatSach(int soLuongMatSach) {
		this.soLuongMatSach = soLuongMatSach;
	}
	public int getSoLuongLamHuSach() {
		return soLuongLamHuSach;
	}
	public void setSoLuongLamHuSach(int soLuongLamHuSach) {
		this.soLuongLamHuSach = soLuongLamHuSach;
	}
	public double getDoanhThu() {
		return doanhThu;
	}
	public void setDoanhThu(double doanhThu) {
		this.doanhThu = doanhThu;
	}
	@Override
	public String toString() {
		return "ThongTinThongKe [thang=" + thang + ", soDocGiaMoi=" + soDocGiaMoi + ", soLuongTraSachTre="
				+ soLuongTraSachTre + ", soLuongMatSach=" + soLuongMatSach + ", soLuongLamHuSach=" + soLuongLamHuSach
				+ ", doanhThu=" + doanhThu + "]";
	}
	
}
