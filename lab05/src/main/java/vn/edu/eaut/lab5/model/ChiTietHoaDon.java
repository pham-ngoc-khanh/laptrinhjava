package vn.edu.eaut.lab5.model;

import java.math.BigDecimal;

public class ChiTietHoaDon {
    private int maHd;
    private int maSp;
    private String tenSp;
    private int soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(int maSp, String tenSp, int soLuong, BigDecimal donGia) {
        this.maSp = maSp;
        this.tenSp = tenSp;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));
    }

    public ChiTietHoaDon(int maHd, int maSp, String tenSp, int soLuong, BigDecimal donGia) {
        this.maHd = maHd;
        this.maSp = maSp;
        this.tenSp = tenSp;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));
    }

    public int getMaHd() {
        return maHd;
    }

    public void setMaHd(int maHd) {
        this.maHd = maHd;
    }

    public int getMaSp() {
        return maSp;
    }

    public void setMaSp(int maSp) {
        this.maSp = maSp;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        if (this.donGia != null) {
            this.thanhTien = this.donGia.multiply(BigDecimal.valueOf(soLuong));
        }
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
        this.thanhTien = donGia.multiply(BigDecimal.valueOf(this.soLuong));
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }
}
