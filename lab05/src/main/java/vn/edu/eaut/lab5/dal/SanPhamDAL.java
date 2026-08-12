package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.SanPham;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAL {
    public List<SanPham> findAll() throws SQLException {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT ma_sp, ten_sp, don_gia, so_luong FROM san_pham";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSp(rs.getInt("ma_sp"));
                sp.setTenSp(rs.getString("ten_sp"));
                sp.setDonGia(rs.getBigDecimal("don_gia"));
                sp.setSoLuong(rs.getInt("so_luong"));
                list.add(sp);
            }
        }
        return list;
    }

    public boolean insert(SanPham sp) throws SQLException {
        String sql = "INSERT INTO san_pham(ten_sp, don_gia, so_luong) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSp());
            ps.setBigDecimal(2, sp.getDonGia());
            ps.setInt(3, sp.getSoLuong());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(SanPham sp) throws SQLException {
        String sql = "UPDATE san_pham SET ten_sp = ?, don_gia = ?, so_luong = ? WHERE ma_sp = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSp());
            ps.setBigDecimal(2, sp.getDonGia());
            ps.setInt(3, sp.getSoLuong());
            ps.setInt(4, sp.getMaSp());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int maSp) throws SQLException {
        String sql = "DELETE FROM san_pham WHERE ma_sp = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSp);
            return ps.executeUpdate() > 0;
        }
    }

    public List<SanPham> searchByName(String keyword) throws SQLException {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT ma_sp, ten_sp, don_gia, so_luong FROM san_pham WHERE ten_sp LIKE ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setMaSp(rs.getInt("ma_sp"));
                    sp.setTenSp(rs.getString("ten_sp"));
                    sp.setDonGia(rs.getBigDecimal("don_gia"));
                    sp.setSoLuong(rs.getInt("so_luong"));
                    list.add(sp);
                }
            }
        }
        return list;
    }
}
