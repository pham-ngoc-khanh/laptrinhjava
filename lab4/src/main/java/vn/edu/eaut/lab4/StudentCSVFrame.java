package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class StudentCSVFrame extends JFrame {
    private JButton btnChooseFile;
    private JButton btnLoad;
    private JLabel lblFile;
    private JLabel lblStats;
    private JProgressBar progressBar;
    private JTable table;
    private DefaultTableModel tableModel;
    private File selectedFile;
    private SwingWorker<List<String[]>, String[]> worker;

    public StudentCSVFrame() {
        setTitle("Bài 8 - Đọc CSV điểm sinh viên & Thống kê");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        btnChooseFile = new JButton("Chọn file CSV");
        btnLoad = new JButton("Đọc và thống kê");
        lblFile = new JLabel("File: Chưa chọn file");
        lblStats = new JLabel(" ");
        lblStats.setFont(new Font("Arial", Font.BOLD, 14));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        String[] columns = {"Mã SV", "Họ Tên", "Điểm"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(btnChooseFile);
        topPanel.add(btnLoad);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.add(lblFile);
        infoPanel.add(lblStats);

        JPanel northPanel = new JPanel(new BorderLayout(10, 10));
        northPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(infoPanel, BorderLayout.CENTER);
        northPanel.add(progressBar, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnChooseFile.addActionListener(e -> chooseFile());
        btnLoad.addActionListener(e -> loadCSV());
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Tệp CSV (*.csv)", "csv"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            lblFile.setText("File: " + selectedFile.getAbsolutePath());
        }
    }

    private void loadCSV() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file CSV trước");
            return;
        }

        btnLoad.setEnabled(false);
        progressBar.setValue(0);
        tableModel.setRowCount(0);
        lblStats.setText("Đang đọc file...");

        worker = new SwingWorker<List<String[]>, String[]>() {
            @Override
            protected List<String[]> doInBackground() throws Exception {
                List<String[]> rows = new ArrayList<>();
                long totalBytes = Files.size(selectedFile.toPath());
                long readBytes = 0;
                boolean isFirst = true;
                try (BufferedReader reader = Files.newBufferedReader(
                        selectedFile.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (isCancelled()) return rows;
                        readBytes += line.getBytes(StandardCharsets.UTF_8).length + 1;
                        if (isFirst) {
                            isFirst = false;
                            int progress = totalBytes == 0
                                    ? 100 : (int) Math.min(100, (readBytes * 100 / totalBytes));
                            setProgress(progress);
                            continue;
                        }
                        line = line.trim();
                        if (line.isEmpty()) continue;
                        String[] parts = line.split(",");
                        if (parts.length >= 3) {
                            String[] row = new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()};
                            rows.add(row);
                            publish(row);
                        }
                        int progress = totalBytes == 0
                                ? 100 : (int) Math.min(100, (readBytes * 100 / totalBytes));
                        setProgress(progress);
                    }
                }
                return rows;
            }

            @Override
            protected void process(List<String[]> chunks) {
                for (String[] row : chunks) {
                    tableModel.addRow(row);
                }
            }

            @Override
            protected void done() {
                try {
                    List<String[]> rows = get();
                    if (rows.isEmpty()) {
                        lblStats.setText("Không có dữ liệu sinh viên");
                    } else {
                        double total = 0;
                        double max = -1;
                        String topStudent = "";
                        for (String[] row : rows) {
                            try {
                                double mark = Double.parseDouble(row[2]);
                                total += mark;
                                if (mark > max) {
                                    max = mark;
                                    topStudent = row[1] + " (" + row[0] + ") - " + mark;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }
                        double avg = total / rows.size();
                        lblStats.setText(String.format("Số SV: %d | Điểm TB: %.2f | Điểm cao nhất: %s",
                                rows.size(), avg, topStudent));
                    }
                } catch (Exception ex) {
                    lblStats.setText("Lỗi khi đọc file CSV");
                }
                btnLoad.setEnabled(true);
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
