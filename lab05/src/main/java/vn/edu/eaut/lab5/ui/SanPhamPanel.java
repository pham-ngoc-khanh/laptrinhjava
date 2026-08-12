package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.SanPhamBUS;
import vn.edu.eaut.lab5.model.SanPham;
import vn.edu.eaut.lab5.util.MessageUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class SanPhamPanel extends JPanel {
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private JTextField txtMaSp, txtTenSp, txtDonGia, txtSoLuong, txtTimKiem;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedMaSp = 0;

    public SanPhamPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã SP:"), gbc);
        txtMaSp = new JTextField(15);
        txtMaSp.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(txtMaSp, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên SP:"), gbc);
        txtTenSp = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtTenSp, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Đơn giá:"), gbc);
        txtDonGia = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(txtDonGia, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Số lượng:"), gbc);
        txtSoLuong = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(txtSoLuong, gbc);

        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLamMoi);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(25);
        JButton btnTim = new JButton("Tìm");
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTim);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);
        topPanel.add(searchPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] cols = {"Mã SP", "Tên SP", "Đơn giá", "Số lượng"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));
        add(scrollPane, BorderLayout.CENTER);

        // Events
        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTim.addActionListener(e -> timKiem());
        txtTimKiem.addActionListener(e -> timKiem());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    fillFormFromRow(row);
                }
            }
        });
    }

    private void loadData() {
        try {
            List<SanPham> list = sanPhamBUS.findAll();
            fillTable(list);
        } catch (SQLException e) {
            MessageUtil.showError(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void fillTable(List<SanPham> list) {
        tableModel.setRowCount(0);
        for (SanPham sp : list) {
            tableModel.addRow(new Object[]{
                    sp.getMaSp(), sp.getTenSp(), sp.getDonGia(), sp.getSoLuong()
            });
        }
    }

    private void fillFormFromRow(int row) {
        selectedMaSp = (int) tableModel.getValueAt(row, 0);
        txtMaSp.setText(String.valueOf(selectedMaSp));
        txtTenSp.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtDonGia.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtSoLuong.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    private void themSanPham() {
        try {
            SanPham sp = getFormData(0);
            if (sanPhamBUS.save(sp)) {
                MessageUtil.showInfo(this, "Thêm thành công!");
                lamMoi();
            }
        } catch (Exception e) {
            MessageUtil.showError(this, e.getMessage());
        }
    }

    private void suaSanPham() {
        if (selectedMaSp == 0) {
            MessageUtil.showWarning(this, "Vui lòng chọn sản phẩm cần sửa!");
            return;
        }
        try {
            SanPham sp = getFormData(selectedMaSp);
            if (sanPhamBUS.save(sp)) {
                MessageUtil.showInfo(this, "Sửa thành công!");
                lamMoi();
            }
        } catch (Exception e) {
            MessageUtil.showError(this, e.getMessage());
        }
    }

    private void xoaSanPham() {
        if (selectedMaSp == 0) {
            MessageUtil.showWarning(this, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }
        if (!MessageUtil.confirm(this, "Bạn chắc chắn muốn xóa?")) return;
        try {
            if (sanPhamBUS.delete(selectedMaSp)) {
                MessageUtil.showInfo(this, "Xóa thành công!");
                lamMoi();
            }
        } catch (Exception e) {
            MessageUtil.showError(this, e.getMessage());
        }
    }

    private void lamMoi() {
        selectedMaSp = 0;
        txtMaSp.setText("");
        txtTenSp.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("");
        txtTimKiem.setText("");
        loadData();
    }

    private void timKiem() {
        String kw = txtTimKiem.getText().trim();
        if (kw.isEmpty()) {
            loadData();
            return;
        }
        try {
            List<SanPham> list = sanPhamBUS.searchByName(kw);
            fillTable(list);
        } catch (SQLException e) {
            MessageUtil.showError(this, "Lỗi tìm kiếm: " + e.getMessage());
        }
    }

    private SanPham getFormData(int maSp) {
        String ten = txtTenSp.getText().trim();
        String donGiaStr = txtDonGia.getText().trim();
        String soLuongStr = txtSoLuong.getText().trim();
        if (ten.isEmpty()) throw new IllegalArgumentException("Tên SP không được rỗng!");
        BigDecimal donGia;
        int soLuong;
        try {
            donGia = new BigDecimal(donGiaStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Đơn giá phải là số!");
        }
        try {
            soLuong = Integer.parseInt(soLuongStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số lượng phải là số nguyên!");
        }
        return new SanPham(maSp, ten, donGia, soLuong);
    }
}
