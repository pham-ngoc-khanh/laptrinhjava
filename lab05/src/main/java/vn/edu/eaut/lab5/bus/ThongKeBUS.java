package vn.edu.eaut.lab5.bus;

import vn.edu.eaut.lab5.dal.ThongKeDAL;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

public class ThongKeBUS {
    private final ThongKeDAL thongKeDAL = new ThongKeDAL();

    public BigDecimal getTongDoanhThu() throws SQLException {
        return thongKeDAL.getTongDoanhThu();
    }

    public Map<String, BigDecimal> getTopSanPhamBanChay(int limit) throws SQLException {
        if (limit <= 0) {
            limit = 5;
        }
        return thongKeDAL.getTopSanPhamBanChay(limit);
    }
}
