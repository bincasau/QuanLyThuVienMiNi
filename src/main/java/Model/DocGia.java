package Model;

import java.sql.Date;

public class DocGia extends NguoiSuDung {
	private Date ngayTao;

	public DocGia() {
		super();
	}

	public DocGia(String maNguoiDung, String taiKhoan, String matKhau, String email, String soDienThoai,
			String tenNguoiDung, Date ngayTao) {
		super(maNguoiDung, taiKhoan, matKhau, email, soDienThoai, tenNguoiDung);
		this.ngayTao = ngayTao;
	}

	public Date getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}

}
