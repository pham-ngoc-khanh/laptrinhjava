package vn.edu.eaut.lab5.bus;

import vn.edu.eaut.lab5.dal.ChiTietHoaDonDAL;
import vn.edu.eaut.lab5.dal.HoaDonDAL;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import vn.edu.eaut.lab5.model.HoaDon;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class HoaDonBUS {
    private final HoaDonDAL hoaDonDAL = new HoaDonDAL();
    private final ChiTietHoaDonDAL chiTietHoaDonDAL = new ChiTietHoaDonDAL();

    public List<HoaDon> findAll() throws SQLException {
        return hoaDonDAL.findAll();
    }

    public List<HoaDon> findByDate(Date from, Date to) throws SQLException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Ngay khong duoc rong");
        }
        return hoaDonDAL.findByDate(from, to);
    }

    public List<ChiTietHoaDon> getChiTietByMaHd(int maHd) throws SQLException {
        return chiTietHoaDonDAL.findByMaHd(maHd);
    }

    public int createHoaDon(HoaDon hd, List<ChiTietHoaDon> chiTietList) throws SQLException {
        validateHoaDon(hd);
        if (chiTietList == null || chiTietList.isEmpty()) {
            throw new IllegalArgumentException("Hoa don phai co it nhat mot chi tiet");
        }

        Connection conn = null;
        try {
            conn = vn.edu.eaut.lab5.config.DBHelper.getConnection();
            conn.setAutoCommit(false);

            BigDecimal tongTien = BigDecimal.ZERO;
            for (ChiTietHoaDon ct : chiTietList) {
                validateChiTiet(ct);
                tongTien = tongTien.add(ct.getThanhTien());
            }
            hd.setTongTien(tongTien);

            int maHd = hoaDonDAL.insertReturnId(hd);
            if (maHd <= 0) {
                conn.rollback();
                throw new SQLException("Them hoa don that bai");
            }

            for (ChiTietHoaDon ct : chiTietList) {
                ct.setMaHd(maHd);
                if (!chiTietHoaDonDAL.insert(ct)) {
                    conn.rollback();
                    throw new SQLException("Them chi tiet hoa don that bai");
                }
            }

            conn.commit();
            return maHd;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public boolean deleteHoaDon(int maHd) throws SQLException {
        if (maHd <= 0) {
            throw new IllegalArgumentException("Ma hoa don khong hop le");
        }
        Connection conn = null;
        try {
            conn = vn.edu.eaut.lab5.config.DBHelper.getConnection();
            conn.setAutoCommit(false);

            chiTietHoaDonDAL.deleteByMaHd(maHd);
            boolean result = hoaDonDAL.delete(maHd);

            conn.commit();
            return result;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private void validateHoaDon(HoaDon hd) {
        if (hd.getNgayLap() == null) {
            throw new IllegalArgumentException("Ngay lap khong duoc rong");
        }
        if (hd.getMaKh() <= 0) {
            throw new IllegalArgumentException("Ma khach hang khong hop le");
        }
    }

    private void validateChiTiet(ChiTietHoaDon ct) {
        if (ct.getMaSp() <= 0) {
            throw new IllegalArgumentException("Ma san pham khong hop le");
        }
        if (ct.getSoLuong() <= 0) {
            throw new IllegalArgumentException("So luong phai lon hon 0");
        }
        if (ct.getDonGia() == null || ct.getDonGia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Don gia phai lon hon 0");
        }
        if (ct.getThanhTien() == null || ct.getThanhTien().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Thanh tien phai lon hon 0");
        }
    }
}
