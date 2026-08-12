package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.HoaDonBUS;
import vn.edu.eaut.lab5.bus.ThongKeBUS;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import vn.edu.eaut.lab5.model.HoaDon;
import vn.edu.eaut.lab5.util.MessageUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ThongKePanel extends JPanel {
    private final ThongKeBUS thongKeBUS = new ThongKeBUS();
    private final HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private JTextField txtTongDoanhThu;
    private JTable tableTop;
    private DefaultTableModel topTableModel;
    private JTextField txtTuNgay, txtDenNgay;
    private JTable tableHD;
    private DefaultTableModel hdTableModel;

    public ThongKePanel() {
        initComponents();
        loadThongKe();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel: Doanh thu + Top sản phẩm
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        JPanel doanhThuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        doanhThuPanel.setBorder(BorderFactory.createTitledBorder("Tổng doanh thu"));
        doanhThuPanel.add(new JLabel("Tổng doanh thu:"));
        txtTongDoanhThu = new JTextField(20);
        txtTongDoanhThu.setEditable(false);
        doanhThuPanel.add(txtTongDoanhThu);
        JButton btnRefresh = new JButton("Làm mới");
        doanhThuPanel.add(btnRefresh);
        topPanel.add(doanhThuPanel, BorderLayout.NORTH);

        String[] topCols = {"Tên sản phẩm", "Tổng tiền"};
        topTableModel = new DefaultTableModel(topCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableTop = new JTable(topTableModel);
        JScrollPane spTop = new JScrollPane(tableTop);
        spTop.setBorder(BorderFactory.createTitledBorder("Top 5 sản phẩm bán chạy"));
        spTop.setPreferredSize(new Dimension(0, 200));
        topPanel.add(spTop, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Center panel: Tim kiem hoa don theo ngay
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Tìm hóa đơn theo ngày"));
        filterPanel.add(new JLabel("Từ ngày (yyyy-MM-dd):"));
        txtTuNgay = new JTextField(12);
        filterPanel.add(txtTuNgay);
        filterPanel.add(new JLabel("Đến ngày:"));
        txtDenNgay = new JTextField(12);
        filterPanel.add(txtDenNgay);
        JButton btnTimHD = new JButton("Tìm");
        filterPanel.add(btnTimHD);
        JButton btnXemCT = new JButton("Xem chi tiết");
        filterPanel.add(btnXemCT);
        centerPanel.add(filterPanel, BorderLayout.NORTH);

        String[] hdCols = {"Mã HD", "Ngày lập", "Mã KH", "Tổng tiền"};
        hdTableModel = new DefaultTableModel(hdCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHD = new JTable(hdTableModel);
        tableHD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane spHD = new JScrollPane(tableHD);
        spHD.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn"));
        centerPanel.add(spHD, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Events
        btnRefresh.addActionListener(e -> loadThongKe());
        btnTimHD.addActionListener(e -> timHoaDonTheoNgay());
        btnXemCT.addActionListener(e -> xemChiTietHoaDon());
    }

    private void loadThongKe() {
        try {
            BigDecimal dt = thongKeBUS.getTongDoanhThu();
            txtTongDoanhThu.setText(dt.toString());

            Map<String, BigDecimal> top = thongKeBUS.getTopSanPhamBanChay(5);
            topTableModel.setRowCount(0);
            if (top.isEmpty()) {
                topTableModel.addRow(new Object[]{"(Chưa có dữ liệu)", "0"});
            } else {
                for (Map.Entry<String, BigDecimal> entry : top.entrySet()) {
                    topTableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
                }
            }

            List<HoaDon> list = hoaDonBUS.findAll();
            hdTableModel.setRowCount(0);
            for (HoaDon hd : list) {
                hdTableModel.addRow(new Object[]{hd.getMaHd(), hd.getNgayLap(), hd.getMaKh(), hd.getTongTien()});
            }
        } catch (SQLException e) {
            MessageUtil.showError(this, "Lỗi tải thống kê: " + e.getMessage());
        }
    }

    private void timHoaDonTheoNgay() {
        String tu = txtTuNgay.getText().trim();
        String den = txtDenNgay.getText().trim();
        if (tu.isEmpty() || den.isEmpty()) {
            MessageUtil.showWarning(this, "Vui lòng nhập đủ khoảng ngày!");
            return;
        }
        try {
            Date from = Date.valueOf(tu);
            Date to = Date.valueOf(den);
            List<HoaDon> list = hoaDonBUS.findByDate(from, to);
            hdTableModel.setRowCount(0);
            for (HoaDon hd : list) {
                hdTableModel.addRow(new Object[]{hd.getMaHd(), hd.getNgayLap(), hd.getMaKh(), hd.getTongTien()});
            }
            if (list.isEmpty()) {
                MessageUtil.showInfo(this, "Không tìm thấy hóa đơn trong khoảng thời gian này!");
            }
        } catch (IllegalArgumentException e) {
            MessageUtil.showError(this, "Định dạng ngày không hợp lệ! (yyyy-MM-dd)");
        } catch (SQLException e) {
            MessageUtil.showError(this, "Lỗi tìm kiếm: " + e.getMessage());
        }
    }

    private void xemChiTietHoaDon() {
        int row = tableHD.getSelectedRow();
        if (row < 0) {
            MessageUtil.showWarning(this, "Vui lòng chọn hóa đơn!");
            return;
        }
        int maHd = (int) hdTableModel.getValueAt(row, 0);
        try {
            List<ChiTietHoaDon> list = hoaDonBUS.getChiTietByMaHd(maHd);
            StringBuilder sb = new StringBuilder();
            sb.append("Chi tiết hóa đơn #").append(maHd).append("\n");
            sb.append(String.format("%-8s %-25s %-10s %-12s %-12s%n",
                    "Ma SP", "Ten SP", "So luong", "Don gia", "Thanh tien"));
            sb.append("------------------------------------------------------------------------\n");
            BigDecimal tong = BigDecimal.ZERO;
            for (ChiTietHoaDon ct : list) {
                sb.append(String.format("%-8d %-25s %-10d %-12s %-12s%n",
                        ct.getMaSp(), ct.getTenSp() != null ? ct.getTenSp() : "",
                        ct.getSoLuong(), ct.getDonGia(), ct.getThanhTien()));
                tong = tong.add(ct.getThanhTien() != null ? ct.getThanhTien() : BigDecimal.ZERO);
            }
            sb.append("\nTổng tiền: ").append(tong);
            JTextArea ta = new JTextArea(sb.toString(), 20, 65);
            ta.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(ta),
                    "Chi tiết hóa đơn #" + maHd, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            MessageUtil.showError(this, "Lỗi: " + e.getMessage());
        }
    }
}
