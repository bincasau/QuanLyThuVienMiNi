package Model;

import java.util.Date;

public class DocGia {

	
    private String maNguoiDung;
    private String tenNguoiDung;
    private String taiKhoan;
    private String matKhau;
    private String email;
    private String soDienThoai;
    private Date ngayTao;

    public DocGia(String maNguoiDung, String tenNguoiDung, String taiKhoan, String matKhau, String email, String soDienThoai, Date ngayTao) {
        this.maNguoiDung = maNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.ngayTao = ngayTao;
    }
    
    public DocGia() {
    }

    // Getters

    public void setMaNguoiDung(String maNguoiDung) {
    this.maNguoiDung = maNguoiDung;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

	public void setTenNguoiDung(String tenNguoiDung) {
		this.tenNguoiDung = tenNguoiDung;
	}

	public void setTaiKhoan(String taiKhoan) {
		this.taiKhoan = taiKhoan;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public void setNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}
	
    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getEmail() {
        return email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public Date getNgayTao() {
        return ngayTao;
    }
}