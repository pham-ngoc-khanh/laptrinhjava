package vn.edu.eaut.lab3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bai08QuanLySinhVien extends JFrame {
    private final List<SinhVien> danhSachSV = new ArrayList<>();
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField txtMaSV = new JTextField(15);
    private final JTextField txtHoTen = new JTextField(20);
    private final JTextField txtNamSinh = new JTextField(10);
    private final JTextField txtChuyenNganh = new JTextField(20);
    private final JTextField txtDiemTB = new JTextField(10);

    private int selectedRow = -1;

    public Bai08QuanLySinhVien() {
        setTitle("Bài 8 - Quản lý sinh viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        String[] columns = {"Mã SV", "Họ tên", "Năm sinh", "Chuyên ngành", "Điểm TB", "Xếp loại"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách sinh viên"));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Mã sinh viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        inputPanel.add(txtMaSV, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        inputPanel.add(new JLabel("Họ và tên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        inputPanel.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        inputPanel.add(new JLabel("Năm sinh:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        inputPanel.add(txtNamSinh, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        inputPanel.add(new JLabel("Chuyên ngành:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        inputPanel.add(txtChuyenNganh, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        inputPanel.add(new JLabel("Điểm trung bình:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        inputPanel.add(txtDiemTB, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");
        JButton btnRefresh = new JButton("Làm trống");
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        btnPanel.add(btnRefresh);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(btnPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> themSinhVien());
        btnEdit.addActionListener(e -> suaSinhVien());
        btnDelete.addActionListener(e -> xoaSinhVien());
        btnClear.addActionListener(e -> lamMoiForm());
        btnRefresh.addActionListener(e -> lamTrongForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    dayDuLieuLenForm(selectedRow);
                }
            }
        });

        khoiTaoDuLieuMau();
        capNhatTable();

        setSize(750, 550);
        setLocationRelativeTo(null);
    }

    private void khoiTaoDuLieuMau() {
        danhSachSV.add(new SinhVien("SV001", "Nguyễn Văn An", 2002, "Công nghệ thông tin", 8.75));
        danhSachSV.add(new SinhVien("SV002", "Trần Thị Bình", 2003, "Kỹ thuật phần mềm", 7.55));
        danhSachSV.add(new SinhVien("SV003", "Lê Hoàng Cường", 2001, "Hệ thống thông tin", 6.25));
        danhSachSV.add(new SinhVien("SV004", "Phạm Thị Dung", 2004, "Khoa học máy tính", 4.20));
    }

    private void capNhatTable() {
        tableModel.setRowCount(0);
        for (SinhVien sv : danhSachSV) {
            Object[] row = {
                    sv.getMaSV(),
                    sv.getHoTen(),
                    sv.getNamSinh(),
                    sv.getChuyenNganh(),
                    String.format("%.2f", sv.getDiemTB()),
                    xepLoaiSinhVien(sv.getDiemTB())
            };
            tableModel.addRow(row);
        }
    }

    private String xepLoaiSinhVien(double diemTB) {
        if (diemTB >= 8.5) return "Giỏi";
        if (diemTB >= 7.0) return "Khá";
        if (diemTB >= 5.0) return "Trung bình";
        return "Yếu";
    }

    private void dayDuLieuLenForm(int row) {
        SinhVien sv = danhSachSV.get(row);
        txtMaSV.setText(sv.getMaSV());
        txtHoTen.setText(sv.getHoTen());
        txtNamSinh.setText(String.valueOf(sv.getNamSinh()));
        txtChuyenNganh.setText(sv.getChuyenNganh());
        txtDiemTB.setText(String.valueOf(sv.getDiemTB()));
    }

    private void themSinhVien() {
        try {
            SinhVien sv = layDuLieuTuForm();
            if (sv == null) return;

            for (SinhVien s : danhSachSV) {
                if (s.getMaSV().equalsIgnoreCase(sv.getMaSV())) {
                    JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại!");
                    txtMaSV.requestFocus();
                    return;
                }
            }

            danhSachSV.add(sv);
            capNhatTable();
            lamTrongForm();
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Năm sinh và Điểm TB phải là số hợp lệ!");
        }
    }

    private void suaSinhVien() {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên trong bảng!");
            return;
        }

        try {
            SinhVien svMoi = layDuLieuTuForm();
            if (svMoi == null) return;

            String maCu = danhSachSV.get(selectedRow).getMaSV();
            if (!svMoi.getMaSV().equalsIgnoreCase(maCu)) {
                for (SinhVien s : danhSachSV) {
                    if (s.getMaSV().equalsIgnoreCase(svMoi.getMaSV())) {
                        JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại!");
                        txtMaSV.requestFocus();
                        return;
                    }
                }
            }

            danhSachSV.set(selectedRow, svMoi);
            capNhatTable();
            lamTrongForm();
            selectedRow = -1;
            table.clearSelection();
            JOptionPane.showMessageDialog(this, "Cập nhật sinh viên thành công!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Năm sinh và Điểm TB phải là số hợp lệ!");
        }
    }

    private void xoaSinhVien() {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên trong bảng!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa sinh viên \"" + danhSachSV.get(selectedRow).getHoTen() + "\" không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            danhSachSV.remove(selectedRow);
            capNhatTable();
            lamTrongForm();
            selectedRow = -1;
            table.clearSelection();
            JOptionPane.showMessageDialog(this, "Xóa sinh viên thành công!");
        }
    }

    private SinhVien layDuLieuTuForm() {
        String maSV = txtMaSV.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String namSinhStr = txtNamSinh.getText().trim();
        String chuyenNganh = txtChuyenNganh.getText().trim();
        String diemTBStr = txtDiemTB.getText().trim();

        if (maSV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã sinh viên!");
            txtMaSV.requestFocus();
            return null;
        }
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Họ và tên!");
            txtHoTen.requestFocus();
            return null;
        }
        if (namSinhStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Năm sinh!");
            txtNamSinh.requestFocus();
            return null;
        }
        if (chuyenNganh.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Chuyên ngành!");
            txtChuyenNganh.requestFocus();
            return null;
        }
        if (diemTBStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Điểm trung bình!");
            txtDiemTB.requestFocus();
            return null;
        }

        int namSinh = Integer.parseInt(namSinhStr);
        double diemTB = Double.parseDouble(diemTBStr);

        if (namSinh < 1900 || namSinh > 2100) {
            JOptionPane.showMessageDialog(this, "Năm sinh không hợp lệ!");
            txtNamSinh.requestFocus();
            return null;
        }
        if (diemTB < 0 || diemTB > 10) {
            JOptionPane.showMessageDialog(this, "Điểm trung bình phải từ 0 đến 10!");
            txtDiemTB.requestFocus();
            return null;
        }

        return new SinhVien(maSV, hoTen, namSinh, chuyenNganh, diemTB);
    }

    private void lamTrongForm() {
        txtMaSV.setText("");
        txtHoTen.setText("");
        txtNamSinh.setText("");
        txtChuyenNganh.setText("");
        txtDiemTB.setText("");
        txtMaSV.requestFocus();
    }

    private void lamMoiForm() {
        lamTrongForm();
        selectedRow = -1;
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai08QuanLySinhVien().setVisible(true));
    }
}
