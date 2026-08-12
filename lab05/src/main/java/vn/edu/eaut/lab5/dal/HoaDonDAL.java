package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.HoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAL {
    public List<HoaDon> findAll() throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT ma_hd, ngay_lap, ma_kh, tong_tien FROM hoa_don";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHd(rs.getInt("ma_hd"));
                hd.setNgayLap(rs.getDate("ngay_lap"));
                hd.setMaKh(rs.getInt("ma_kh"));
                hd.setTongTien(rs.getBigDecimal("tong_tien"));
                list.add(hd);
            }
        }
        return list;
    }

    public int insertReturnId(HoaDon hd) throws SQLException {
        String sql = "INSERT INTO hoa_don(ngay_lap, ma_kh, tong_tien) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, hd.getNgayLap());
            ps.setInt(2, hd.getMaKh());
            ps.setBigDecimal(3, hd.getTongTien() == null ? BigDecimal.ZERO : hd.getTongTien());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean updateTongTien(int maHd, BigDecimal tongTien) throws SQLException {
        String sql = "UPDATE hoa_don SET tong_tien = ? WHERE ma_hd = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, tongTien);
            ps.setInt(2, maHd);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int maHd) throws SQLException {
        String sql = "DELETE FROM hoa_don WHERE ma_hd = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHd);
            return ps.executeUpdate() > 0;
        }
    }

    public List<HoaDon> findByDate(Date from, Date to) throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT ma_hd, ngay_lap, ma_kh, tong_tien FROM hoa_don WHERE ngay_lap BETWEEN ? AND ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, from);
            ps.setDate(2, to);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHd(rs.getInt("ma_hd"));
                    hd.setNgayLap(rs.getDate("ngay_lap"));
                    hd.setMaKh(rs.getInt("ma_kh"));
                    hd.setTongTien(rs.getBigDecimal("tong_tien"));
                    list.add(hd);
                }
            }
        }
        return list;
    }
}
