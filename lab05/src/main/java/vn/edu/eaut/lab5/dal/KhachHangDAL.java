package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.model.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAL {
    public List<KhachHang> findAll() throws SQLException {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT ma_kh, ten_kh, sdt, dia_chi FROM khach_hang";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKh(rs.getInt("ma_kh"));
                kh.setTenKh(rs.getString("ten_kh"));
                kh.setSdt(rs.getString("sdt"));
                kh.setDiaChi(rs.getString("dia_chi"));
                list.add(kh);
            }
        }
        return list;
    }

    public boolean insert(KhachHang kh) throws SQLException {
        String sql = "INSERT INTO khach_hang(ten_kh, sdt, dia_chi) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKh());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getDiaChi());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(KhachHang kh) throws SQLException {
        String sql = "UPDATE khach_hang SET ten_kh = ?, sdt = ?, dia_chi = ? WHERE ma_kh = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKh());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getDiaChi());
            ps.setInt(4, kh.getMaKh());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int maKh) throws SQLException {
        String sql = "DELETE FROM khach_hang WHERE ma_kh = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKh);
            return ps.executeUpdate() > 0;
        }
    }

    public List<KhachHang> searchByName(String keyword) throws SQLException {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT ma_kh, ten_kh, sdt, dia_chi FROM khach_hang WHERE ten_kh LIKE ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKh(rs.getInt("ma_kh"));
                    kh.setTenKh(rs.getString("ten_kh"));
                    kh.setSdt(rs.getString("sdt"));
                    kh.setDiaChi(rs.getString("dia_chi"));
                    list.add(kh);
                }
            }
        }
        return list;
    }
}
