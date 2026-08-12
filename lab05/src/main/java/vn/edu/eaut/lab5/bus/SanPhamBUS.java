package vn.edu.eaut.lab5.bus;

import vn.edu.eaut.lab5.dal.SanPhamDAL;
import vn.edu.eaut.lab5.model.SanPham;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class SanPhamBUS {
    private final SanPhamDAL sanPhamDAL = new SanPhamDAL();

    public List<SanPham> findAll() throws SQLException {
        return sanPhamDAL.findAll();
    }

    public boolean save(SanPham sp) throws SQLException {
        validate(sp);
        if (sp.getMaSp() == 0) {
            return sanPhamDAL.insert(sp);
        }
        return sanPhamDAL.update(sp);
    }

    public boolean delete(int maSp) throws SQLException {
        if (maSp <= 0) {
            throw new IllegalArgumentException("Ma san pham khong hop le");
        }
        return sanPhamDAL.delete(maSp);
    }

    public List<SanPham> searchByName(String keyword) throws SQLException {
        return sanPhamDAL.searchByName(keyword);
    }

    private void validate(SanPham sp) {
        if (sp.getTenSp() == null || sp.getTenSp().trim().isEmpty()) {
            throw new IllegalArgumentException("Ten san pham khong duoc rong");
        }
        if (sp.getDonGia() == null || sp.getDonGia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Don gia phai lon hon 0");
        }
        if (sp.getSoLuong() < 0) {
            throw new IllegalArgumentException("So luong khong duoc am");
        }
    }
}
