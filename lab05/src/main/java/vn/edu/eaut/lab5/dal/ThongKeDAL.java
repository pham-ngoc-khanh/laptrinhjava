package vn.edu.eaut.lab5.dal;

import vn.edu.eaut.lab5.config.DBHelper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThongKeDAL {
    public BigDecimal getTongDoanhThu() throws SQLException {
        String sql = "SELECT COALESCE(SUM(tong_tien), 0) FROM hoa_don";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    public Map<String, BigDecimal> getTopSanPhamBanChay(int limit) throws SQLException {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        String sql = "SELECT sp.ten_sp, SUM(ct.so_luong) AS sl_ban, SUM(ct.thanh_tien) AS tong_tien " +
                "FROM chi_tiet_hoa_don ct " +
                "JOIN san_pham sp ON ct.ma_sp = sp.ma_sp " +
                "GROUP BY ct.ma_sp, sp.ten_sp " +
                "ORDER BY sl_ban DESC LIMIT ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String ten = rs.getString("ten_sp") + " (SL: " + rs.getInt("sl_ban") + ")";
                    result.put(ten, rs.getBigDecimal("tong_tien"));
                }
            }
        }
        return result;
    }
}
