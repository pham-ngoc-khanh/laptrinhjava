package vn.edu.eaut.lab5;

import vn.edu.eaut.lab5.config.DBHelper;
import vn.edu.eaut.lab5.bus.SanPhamBUS;
import vn.edu.eaut.lab5.bus.KhachHangBUS;
import vn.edu.eaut.lab5.model.SanPham;
import vn.edu.eaut.lab5.model.KhachHang;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

public class TestConnection {
    public static void main(String[] args) {
        StringBuilder result = new StringBuilder();
        result.append("=== KET QUA KIEM TRA KET NOI CSDL ===\n\n");

        try {
            result.append("1. Kiem tra ket noi...\n");
            try (Connection conn = DBHelper.getConnection()) {
                if (conn != null) {
                    result.append("   [OK] Ket noi CSDL thanh cong!\n\n");
                }
            } catch (Exception e) {
                result.append("   [FAIL] Ket noi CSDL that bai: ").append(e.getMessage()).append("\n\n");
                writeResult(result.toString());
                return;
            }

            result.append("2. Kiem tra du lieu san pham...\n");
            try {
                SanPhamBUS spBus = new SanPhamBUS();
                List<SanPham> listSp = spBus.findAll();
                result.append("   [OK] Tim thay ").append(listSp.size()).append(" san pham:\n");
                for (SanPham sp : listSp) {
                    result.append("      - ").append(sp.getTenSp())
                          .append(" | Gia: ").append(sp.getDonGia())
                          .append(" | SL: ").append(sp.getSoLuong()).append("\n");
                }
                result.append("\n");
            } catch (Exception e) {
                result.append("   [FAIL] Loi doc san pham: ").append(e.getMessage()).append("\n\n");
            }

            result.append("3. Kiem tra du lieu khach hang...\n");
            try {
                KhachHangBUS khBus = new KhachHangBUS();
                List<KhachHang> listKh = khBus.findAll();
                result.append("   [OK] Tim thay ").append(listKh.size()).append(" khach hang:\n");
                for (KhachHang kh : listKh) {
                    result.append("      - ").append(kh.getTenKh())
                          .append(" | SDT: ").append(kh.getSdt())
                          .append(" | DC: ").append(kh.getDiaChi()).append("\n");
                }
                result.append("\n");
            } catch (Exception e) {
                result.append("   [FAIL] Loi doc khach hang: ").append(e.getMessage()).append("\n\n");
            }

            result.append("=== TAT CA KIEM TRA HOAN TAT ===");

        } catch (Exception e) {
            result.append("\n[LOI KHONG XAC DINH]: ").append(e.getMessage());
        }

        writeResult(result.toString());
        System.out.println(result.toString());
    }

    private static void writeResult(String content) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("test_result.txt", false))) {
            pw.println(content);
        } catch (Exception e) {
            System.err.println("Khong the ghi file ket qua: " + e.getMessage());
        }
    }
}
