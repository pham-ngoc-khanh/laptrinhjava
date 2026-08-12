package vn.edu.eaut.lab5.bus;

import vn.edu.eaut.lab5.dal.KhachHangDAL;
import vn.edu.eaut.lab5.model.KhachHang;

import java.sql.SQLException;
import java.util.List;

public class KhachHangBUS {
    private final KhachHangDAL khachHangDAL = new KhachHangDAL();

    public List<KhachHang> findAll() throws SQLException {
        return khachHangDAL.findAll();
    }

    public boolean save(KhachHang kh) throws SQLException {
        validate(kh);
        if (kh.getMaKh() == 0) {
            return khachHangDAL.insert(kh);
        }
        return khachHangDAL.update(kh);
    }

    public boolean delete(int maKh) throws SQLException {
        if (maKh <= 0) {
            throw new IllegalArgumentException("Ma khach hang khong hop le");
        }
        return khachHangDAL.delete(maKh);
    }

    public List<KhachHang> searchByName(String keyword) throws SQLException {
        return khachHangDAL.searchByName(keyword);
    }

    private void validate(KhachHang kh) {
        if (kh.getTenKh() == null || kh.getTenKh().trim().isEmpty()) {
            throw new IllegalArgumentException("Ten khach hang khong duoc rong");
        }
        if (kh.getSdt() == null || !kh.getSdt().matches("\\d{1,18}")) {
            throw new IllegalArgumentException("So dien thoai chi gom so va toi da 18 ky tu");
        }
    }
}
