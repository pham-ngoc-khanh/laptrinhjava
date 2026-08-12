package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAL {
    public List<ChiTietHoaDon> findByMaHd(int maHd) throws SQLException {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT ma_hd, ma_sp, so_luong, don_gia, thanh_tien FROM chi_tiet_hoa_don WHERE ma_hd = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHd);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setMaHd(rs.getInt("ma_hd"));
                    ct.setMaSp(rs.getInt("ma_sp"));
                    ct.setSoLuong(rs.getInt("so_luong"));
                    ct.setDonGia(rs.getBigDecimal("don_gia"));
                    ct.setThanhTien(rs.getBigDecimal("thanh_tien"));
                    list.add(ct);
                }
            }
        }
        return list;
    }

    public boolean insert(ChiTietHoaDon ct) throws SQLException {
        String sql = "INSERT INTO chi_tiet_hoa_don(ma_hd, ma_sp, so_luong, don_gia, thanh_tien) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getMaHd());
            ps.setInt(2, ct.getMaSp());
            ps.setInt(3, ct.getSoLuong());
            ps.setBigDecimal(4, ct.getDonGia());
            ps.setBigDecimal(5, ct.getThanhTien());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteByMaHd(int maHd) throws SQLException {
        String sql = "DELETE FROM chi_tiet_hoa_don WHERE ma_hd = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHd);
            return ps.executeUpdate() > 0;
        }
    }
}
