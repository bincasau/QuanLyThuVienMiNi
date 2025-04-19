package Model;

public class NguoiSuDung {
	protected String maNguoiDung;
	protected String taiKhoan;
	protected String matKhau;
	protected String email;
	protected String soDienThoai;
	protected String tenNguoiDung;

	public NguoiSuDung() {
		super();
	}

	public NguoiSuDung(String maNguoiDung, String taiKhoan, String matKhau, String email, String soDienThoai,
			String tenNguoiDung) {
		super();
		this.maNguoiDung = maNguoiDung;
		this.taiKhoan = taiKhoan;
		this.matKhau = matKhau;
		this.email = email;
		this.soDienThoai = soDienThoai;
		this.tenNguoiDung = tenNguoiDung;
	}

	public String getMaNguoiDung() {
		return maNguoiDung;
	}

	public void setMaNguoiDung(String maNguoiDung) {
		this.maNguoiDung = maNguoiDung;
	}

	public String getTaiKhoan() {
		return taiKhoan;
	}

	public void setTaiKhoan(String taiKhoan) {
		this.taiKhoan = taiKhoan;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getTenNguoiDung() {
		return tenNguoiDung;
	}

	public void setTenNguoiDung(String tenNguoiDung) {
		this.tenNguoiDung = tenNguoiDung;
	}
}
