package Model;

public class PhieuPhat {
    private String maPhieuPhat;
    private String loi;
    private double giaTien;
    private String maDocGia;
    private String maSach;

    public PhieuPhat() {
        super();
    }

    public PhieuPhat(String maPhieuPhat, String loi, double giaTien, String maDocGia, String maSach) {
        super();
        this.maPhieuPhat = maPhieuPhat;
        this.loi = loi;
        this.giaTien = giaTien;
        this.maDocGia = maDocGia;
        this.maSach = maSach;
    }

    public String getMaPhieuPhat() {
        return maPhieuPhat;
    }

    public void setMaPhieuPhat(String maPhieuPhat) {
        this.maPhieuPhat = maPhieuPhat;
    }

    public String getLoi() {
        return loi;
    }

    public void setLoi(String loi) {
        this.loi = loi;
    }

    public double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(double giaTien) {
        this.giaTien = giaTien;
    }

    public String getMaDocGia() {
        return maDocGia;
    }

    public void setMaDocGia(String maDocGia) {
        this.maDocGia = maDocGia;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }
}