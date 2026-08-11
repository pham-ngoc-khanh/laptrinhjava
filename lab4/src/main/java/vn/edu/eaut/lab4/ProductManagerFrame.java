package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ProductManagerFrame extends JFrame {
    private JTextField txtMaSP;
    private JTextField txtTenSP;
    private JTextField txtDonGia;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnLoadCSV;
    private JButton btnSaveCSV;
    private JLabel lblStatus;
    private JProgressBar progressBar;
    private JTable table;
    private DefaultTableModel tableModel;
    private File currentFile;

    public ProductManagerFrame() {
        setTitle("Bài 10 - Quản lý sản phẩm bằng file CSV");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        txtMaSP = new JTextField(10);
        txtTenSP = new JTextField(20);
        txtDonGia = new JTextField(12);
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnLoadCSV = new JButton("Đọc file CSV");
        btnSaveCSV = new JButton("Lưu file CSV");
        lblStatus = new JLabel("Sẵn sàng");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        String[] columns = {"Mã SP", "Tên SP", "Đơn giá"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(24);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Mã SP:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtMaSP, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Tên SP:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(txtTenSP, gbc);
        gbc.gridx = 4;
        inputPanel.add(new JLabel("Đơn giá:"), gbc);
        gbc.gridx = 5;
        inputPanel.add(txtDonGia, gbc);

        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        crudPanel.add(btnAdd);
        crudPanel.add(btnEdit);
        crudPanel.add(btnDelete);

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        filePanel.add(btnLoadCSV);
        filePanel.add(btnSaveCSV);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(crudPanel, BorderLayout.CENTER);
        topPanel.add(filePanel, BorderLayout.SOUTH);

        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        southPanel.add(progressBar, BorderLayout.NORTH);
        southPanel.add(lblStatus, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addProduct());
        btnEdit.addActionListener(e -> editProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnLoadCSV.addActionListener(e -> loadCSV());
        btnSaveCSV.addActionListener(e -> saveCSV());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMaSP.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenSP.setText(tableModel.getValueAt(row, 1).toString());
                    txtDonGia.setText(tableModel.getValueAt(row, 2).toString());
                }
            }
        });
    }

    private void setAllButtonsEnabled(boolean enabled) {
        btnAdd.setEnabled(enabled);
        btnEdit.setEnabled(enabled);
        btnDelete.setEnabled(enabled);
        btnLoadCSV.setEnabled(enabled);
        btnSaveCSV.setEnabled(enabled);
    }

    private boolean isMaSPExists(String maSP, int excludeRow) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (i == excludeRow) continue;
            if (tableModel.getValueAt(i, 0).toString().equalsIgnoreCase(maSP)) {
                return true;
            }
        }
        return false;
    }

    private void addProduct() {
        String maSP = txtMaSP.getText().trim();
        String tenSP = txtTenSP.getText().trim();
        String donGiaStr = txtDonGia.getText().trim();

        if (maSP.isEmpty() || tenSP.isEmpty() || donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        try {
            long donGia = Long.parseLong(donGiaStr);
            if (donGia < 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá không được âm");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số nguyên hợp lệ");
            return;
        }
        if (isMaSPExists(maSP, -1)) {
            JOptionPane.showMessageDialog(this, "Mã SP đã tồn tại");
            return;
        }

        tableModel.addRow(new Object[]{maSP, tenSP, donGiaStr});
        clearInputs();
        lblStatus.setText("Đã thêm sản phẩm: " + maSP);
    }

    private void editProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa");
            return;
        }
        String maSP = txtMaSP.getText().trim();
        String tenSP = txtTenSP.getText().trim();
        String donGiaStr = txtDonGia.getText().trim();

        if (maSP.isEmpty() || tenSP.isEmpty() || donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        try {
            long donGia = Long.parseLong(donGiaStr);
            if (donGia < 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá không được âm");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số nguyên hợp lệ");
            return;
        }
        if (isMaSPExists(maSP, row)) {
            JOptionPane.showMessageDialog(this, "Mã SP mới đã tồn tại");
            return;
        }

        tableModel.setValueAt(maSP, row, 0);
        tableModel.setValueAt(tenSP, row, 1);
        tableModel.setValueAt(donGiaStr, row, 2);
        clearInputs();
        lblStatus.setText("Đã cập nhật sản phẩm dòng " + (row + 1));
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sản phẩm " + tableModel.getValueAt(row, 0) + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            clearInputs();
            lblStatus.setText("Đã xóa sản phẩm");
        }
    }

    private void clearInputs() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtDonGia.setText("");
        table.clearSelection();
    }

    private void loadCSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Tệp CSV (*.csv)", "csv"));
        if (currentFile != null) chooser.setSelectedFile(currentFile);
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        final File file = chooser.getSelectedFile();
        currentFile = file;
        setAllButtonsEnabled(false);
        progressBar.setValue(0);
        tableModel.setRowCount(0);
        lblStatus.setText("Đang đọc file: " + file.getName());

        SwingWorker<List<Object[]>, Object[]> worker = new SwingWorker<List<Object[]>, Object[]>() {
            @Override
            protected List<Object[]> doInBackground() throws Exception {
                List<Object[]> rows = new ArrayList<>();
                long totalBytes = Files.size(file.toPath());
                long readBytes = 0;
                boolean firstLine = true;
                try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (isCancelled()) return rows;
                        readBytes += line.getBytes(StandardCharsets.UTF_8).length + 1;
                        if (firstLine) {
                            firstLine = false;
                            String lower = line.trim().toLowerCase();
                            if (lower.startsWith("ma") || lower.contains("mã")) {
                                int prog = totalBytes == 0 ? 100 : (int) Math.min(100, (readBytes * 100 / totalBytes));
                                setProgress(prog);
                                continue;
                            }
                        }
                        line = line.trim();
                        if (line.isEmpty()) continue;
                        String[] parts = line.split(",");
                        if (parts.length >= 3) {
                            Object[] row = new Object[]{
                                    parts[0].trim(), parts[1].trim(), parts[2].trim()
                            };
                            rows.add(row);
                            publish(row);
                        }
                        int prog = totalBytes == 0 ? 100 : (int) Math.min(100, (readBytes * 100 / totalBytes));
                        setProgress(prog);
                    }
                }
                return rows;
            }

            @Override
            protected void process(List<Object[]> chunks) {
                for (Object[] row : chunks) tableModel.addRow(row);
            }

            @Override
            protected void done() {
                try {
                    List<Object[]> rows = get();
                    lblStatus.setText("Đã đọc " + rows.size() + " sản phẩm từ " + file.getName());
                } catch (Exception ex) {
                    lblStatus.setText("Lỗi khi đọc file: " + ex.getMessage());
                }
                setAllButtonsEnabled(true);
                progressBar.setValue(100);
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((int) evt.getNewValue());
            }
        });
        worker.execute();
    }

    private void saveCSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Tệp CSV (*.csv)", "csv"));
        if (currentFile != null) chooser.setSelectedFile(currentFile);
        else chooser.setSelectedFile(new File("san_pham.csv"));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        final File file = chooser.getSelectedFile();
        currentFile = file;

        final List<String> lines = new ArrayList<>();
        lines.add("MaSP,TenSP,DonGia");
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String line = tableModel.getValueAt(i, 0) + ","
                    + tableModel.getValueAt(i, 1) + ","
                    + tableModel.getValueAt(i, 2);
            lines.add(line);
        }

        setAllButtonsEnabled(false);
        progressBar.setValue(0);
        lblStatus.setText("Đang lưu file: " + file.getName());

        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                int total = lines.size();
                try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
                    for (int i = 0; i < total; i++) {
                        if (isCancelled()) return null;
                        writer.write(lines.get(i));
                        writer.newLine();
                        int prog = (int) (((i + 1) * 100.0) / total);
                        setProgress(prog);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                if (isCancelled()) {
                    lblStatus.setText("Đã hủy lưu file");
                } else {
                    try {
                        get();
                        lblStatus.setText("Đã lưu " + (lines.size() - 1) + " sản phẩm vào " + file.getName());
                    } catch (Exception ex) {
                        lblStatus.setText("Lỗi khi lưu file: " + ex.getMessage());
                    }
                }
                setAllButtonsEnabled(true);
                progressBar.setValue(100);
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((int) evt.getNewValue());
            }
        });
        worker.execute();
    }
}
