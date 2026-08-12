package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.bus.KhachHangBUS;
import vn.edu.eaut.lab5.model.KhachHang;
import vn.edu.eaut.lab5.util.MessageUtil;
import vn.edu.eaut.lab5.util.PhoneDocumentFilter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class KhachHangPanel extends JPanel {
    private final KhachHangBUS khachHangBUS = new KhachHangBUS();
    private JTextField txtMaKh, txtTenKh, txtSdt, txtDiaChi, txtTimKiem;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedMaKh = 0;

    public KhachHangPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã KH:"), gbc);
        txtMaKh = new JTextField(15);
        txtMaKh.setEditable(false);
        gbc.gridx = 1;
        formPanel.add(txtMaKh, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên KH:"), gbc);
        txtTenKh = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtTenKh, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("SĐT:"), gbc);
        txtSdt = new JTextField(15);
        ((AbstractDocument) txtSdt.getDocument()).setDocumentFilter(new PhoneDocumentFilter());
        gbc.gridx = 1;
        formPanel.add(txtSdt, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        txtDiaChi = new JTextField(25);
        gbc.gridx = 1;
        formPanel.add(txtDiaChi, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLamMoi);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(25);
        JButton btnTim = new JButton("Tìm");
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTim);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        String[] cols = {"Mã KH", "Tên KH", "SĐT", "Địa chỉ"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
        add(scrollPane, BorderLayout.CENTER);

        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTim.addActionListener(e -> timKiem());
        txtTimKiem.addActionListener(e -> timKiem());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) fillFormFromRow(row);
            }
        });
    }

    private void loadData() {
        try {
            List<KhachHang> list = khachHangBUS.findAll();
            fillTable(list);
        } catch (SQLException e) {
            MessageUtil.showError(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void fillTable(List<KhachHang> list) {
        tableModel.setRowCount(0);
        for (KhachHang kh : list) {
            tableModel.addRow(new Object[]{kh.getMaKh(), kh.getTenKh(), kh.getSdt(), kh.getDiaChi()});
        }
    }

    private void fillFormFromRow(int row) {
        selectedMaKh = (int) tableModel.getValueAt(row, 0);
        txtMaKh.setText(String.valueOf(selectedMaKh));
        txtTenKh.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtSdt.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtDiaChi.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    private void themKhachHang() {
        try {
            KhachHang kh = getFormData(0);
            if (khachHangBUS.save(kh)) {
                MessageUtil.showInfo(this, "Thêm thành công!");
                lamMoi();
            }
        } catch (Exception e) {
            MessageUtil.showError(this, e.getMessage());
        }
    }

    private void suaKhachHang() {
        if (selectedMaKh == 0) {
            MessageUtil.showWarning(this, "Vui lòng chọn khách hàng!");
            return;
        }
        try {
            KhachHang kh = getFormData(selectedMaKh);
            if (khachHangBUS.save(kh)) {
                MessageUtil.showInfo(this, "Sửa thành công!");
                lamMoi();
            }
        } catch (Exception e) {
            MessageUtil.showError(this, e.getMessage());
        }
    }

    private void xoaKhachHang() {
        if (selectedMaKh == 0) {
            MessageUtil.showWarning(this, "Vui lòng chọn khách hàng!");
            return;
        }
        if (!MessageUtil.confirm(this, "Bạn chắc chắn muốn xóa?")) return;
        try {
            if (khachHangBUS.delete(selectedMaKh)) {
                MessageUtil.showInfo(this, "Xóa thành công!");
                lamMoi();
            }
        } catch (Exception e) {
            MessageUtil.showError(this, e.getMessage());
        }
    }

    private void lamMoi() {
        selectedMaKh = 0;
        txtMaKh.setText("");
        txtTenKh.setText("");
        txtSdt.setText("");
        txtDiaChi.setText("");
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
            List<KhachHang> list = khachHangBUS.searchByName(kw);
            fillTable(list);
        } catch (SQLException e) {
            MessageUtil.showError(this, "Lỗi tìm kiếm: " + e.getMessage());
        }
    }

    private KhachHang getFormData(int maKh) {
        String ten = txtTenKh.getText().trim();
        String sdt = txtSdt.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        return new KhachHang(maKh, ten, sdt, diaChi);
    }
}
