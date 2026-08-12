package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.HoaDonBUS;
import vn.edu.eaut.lab5.bus.KhachHangBUS;
import vn.edu.eaut.lab5.bus.SanPhamBUS;
import vn.edu.eaut.lab5.model.ChiTietHoaDon;
import vn.edu.eaut.lab5.model.HoaDon;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.model.SanPham;
import vn.edu.eaut.lab5.util.MessageUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoaDonPanel extends JPanel {
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private final KhachHangBUS khachHangBUS = new KhachHangBUS();
    private final HoaDonBUS hoaDonBUS = new HoaDonBUS();

    private JComboBox<KhachHang> cbKhachHang;
    private JComboBox<SanPham> cbSanPham;
    private JTextField txtSoLuong, txtTongTien;
    private JTable tableChiTiet;
    private DefaultTableModel ctTableModel;
    private DefaultComboBoxModel<KhachHang> khModel;
    private DefaultComboBoxModel<SanPham> spModel;
    private final List<ChiTietHoaDon> chiTietList = new ArrayList<>();

    public HoaDonPanel() {
        initComponents();
        loadComboBoxes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Hoa Don
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Khách hàng:"), gbc);
        cbKhachHang = new JComboBox<>();
        khModel = new DefaultComboBoxModel<>();
        cbKhachHang.setModel(khModel);
        cbKhachHang.setPreferredSize(new Dimension(250, 25));
        gbc.gridx = 1;
        formPanel.add(cbKhachHang, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Sản phẩm:"), gbc);
        cbSanPham = new JComboBox<>();
        spModel = new DefaultComboBoxModel<>();
        cbSanPham.setModel(spModel);
        cbSanPham.setPreferredSize(new Dimension(250, 25));
        gbc.gridx = 1;
        formPanel.add(cbSanPham, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Số lượng:"), gbc);
        txtSoLuong = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(txtSoLuong, gbc);

        JButton btnThemDong = new JButton("Thêm dòng");
        gbc.gridx = 2; gbc.gridy = 2;
        formPanel.add(btnThemDong, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Tổng tiền:"), gbc);
        txtTongTien = new JTextField(15);
        txtTongTien.setEditable(false);
        txtTongTien.setText("0");
        gbc.gridx = 1;
        formPanel.add(txtTongTien, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Table Chi tiet
        String[] cols = {"Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"};
        ctTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableChiTiet = new JTable(ctTableModel);
        tableChiTiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnXoaDong = new JButton("Xóa dòng");
        JButton btnLuuHD = new JButton("Lưu hóa đơn");
        JButton btnLamMoi = new JButton("Làm mới");
        JButton btnXemDSHD = new JButton("Xem DS hóa đơn");
        btnPanel.add(btnXoaDong);
        btnPanel.add(btnLuuHD);
        btnPanel.add(btnLamMoi);
        btnPanel.add(btnXemDSHD);
        add(btnPanel, BorderLayout.SOUTH);

        // Events
        btnThemDong.addActionListener(e -> themDongChiTiet());
        btnXoaDong.addActionListener(e -> xoaDongChiTiet());
        btnLuuHD.addActionListener(e -> luuHoaDon());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnXemDSHD.addActionListener(e -> xemDanhSachHoaDon());
    }

    private void loadComboBoxes() {
        try {
            List<KhachHang> khList = khachHangBUS.findAll();
            khModel.removeAllElements();
            for (KhachHang kh : khList) khModel.addElement(kh);

            List<SanPham> spList = sanPhamBUS.findAll();
            spModel.removeAllElements();
            for (SanPham sp : spList) spModel.addElement(sp);
        } catch (SQLException e) {
            MessageUtil.showError(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void themDongChiTiet() {
        SanPham sp = (SanPham) cbSanPham.getSelectedItem();
        if (sp == null) {
            MessageUtil.showWarning(this, "Vui lòng chọn sản phẩm!");
            return;
        }
        int soLuong;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            MessageUtil.showError(this, "Số lượng phải là số > 0!");
            return;
        }

        ChiTietHoaDon ct = new ChiTietHoaDon(sp.getMaSp(), sp.getTenSp(), soLuong, sp.getDonGia());
        chiTietList.add(ct);
        capNhatBangChiTiet();
        txtSoLuong.setText("");
    }

    private void xoaDongChiTiet() {
        int row = tableChiTiet.getSelectedRow();
        if (row < 0) {
            MessageUtil.showWarning(this, "Vui lòng chọn dòng cần xóa!");
            return;
        }
        chiTietList.remove(row);
        capNhatBangChiTiet();
    }

    private void capNhatBangChiTiet() {
        ctTableModel.setRowCount(0);
        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : chiTietList) {
            ctTableModel.addRow(new Object[]{ct.getMaSp(), ct.getTenSp(),
                    ct.getSoLuong(), ct.getDonGia(), ct.getThanhTien()});
            tong = tong.add(ct.getThanhTien());
        }
        txtTongTien.setText(tong.toString());
    }

    private void luuHoaDon() {
        KhachHang kh = (KhachHang) cbKhachHang.getSelectedItem();
        if (kh == null) {
            MessageUtil.showWarning(this, "Vui lòng chọn khách hàng!");
            return;
        }
        if (chiTietList.isEmpty()) {
            MessageUtil.showWarning(this, "Chưa có chi tiết hóa đơn!");
            return;
        }
        try {
            HoaDon hd = new HoaDon();
            hd.setNgayLap(new Date(System.currentTimeMillis()));
            hd.setMaKh(kh.getMaKh());
            int maHd = hoaDonBUS.createHoaDon(hd, chiTietList);
            if (maHd > 0) {
                MessageUtil.showInfo(this, "Tạo hóa đơn thành công! Mã HD: " + maHd);
                lamMoi();
            }
        } catch (Exception e) {
            MessageUtil.showError(this, "Lỗi lưu hóa đơn: " + e.getMessage());
        }
    }

    private void lamMoi() {
        chiTietList.clear();
        ctTableModel.setRowCount(0);
        txtTongTien.setText("0");
        txtSoLuong.setText("");
        loadComboBoxes();
    }

    private void xemDanhSachHoaDon() {
        try {
            List<HoaDon> list = hoaDonBUS.findAll();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-8s %-15s %-10s %-15s%n", "Ma HD", "Ngay lap", "Ma KH", "Tong tien"));
            sb.append("----------------------------------------------\n");
            for (HoaDon hd : list) {
                sb.append(String.format("%-8d %-15s %-10d %-15s%n",
                        hd.getMaHd(), hd.getNgayLap(), hd.getMaKh(), hd.getTongTien()));
            }
            JTextArea ta = new JTextArea(sb.toString(), 20, 50);
            ta.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Danh sách hóa đơn",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            MessageUtil.showError(this, "Lỗi: " + e.getMessage());
        }
    }
}
