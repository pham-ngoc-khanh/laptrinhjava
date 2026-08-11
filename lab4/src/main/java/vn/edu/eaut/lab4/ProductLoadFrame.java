package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductLoadFrame extends JFrame {
    private JButton btnLoad;
    private JLabel lblStatus;
    private JProgressBar progressBar;
    private JTable table;
    private DefaultTableModel tableModel;
    private SwingWorker<Void, String[]> worker;

    private final String[][] SAMPLE_DATA = {
            {"SP01", "Bàn phím cơ", "250000"},
            {"SP02", "Chuột gaming", "150000"},
            {"SP03", "Màn hình 27 inch", "2500000"},
            {"SP04", "Laptop Dell XPS", "35000000"},
            {"SP05", "Tai nghe Bluetooth", "800000"},
            {"SP06", "Webcam 4K", "1200000"},
            {"SP07", "Ổ cứng SSD 1TB", "1500000"},
            {"SP08", "RAM 16GB", "1800000"},
            {"SP09", "Tản nhiệt nước", "2000000"},
            {"SP10", "Case PC", "3500000"}
    };

    public ProductLoadFrame() {
        setTitle("Bài 9 - Mô phỏng tải danh sách sản phẩm");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        btnLoad = new JButton("Tải sản phẩm");
        lblStatus = new JLabel("Chưa tải dữ liệu");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        String[] columns = {"Mã SP", "Tên SP", "Đơn giá"};
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

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(btnLoad, BorderLayout.WEST);
        topPanel.add(progressBar, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        statusPanel.add(lblStatus, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        btnLoad.addActionListener(e -> loadProducts());
    }

    private void loadProducts() {
        btnLoad.setEnabled(false);
        progressBar.setValue(0);
        tableModel.setRowCount(0);
        lblStatus.setText("Đang tải danh sách sản phẩm...");

        worker = new SwingWorker<Void, String[]>() {
            @Override
            protected Void doInBackground() throws Exception {
                int total = SAMPLE_DATA.length;
                for (int i = 0; i < total; i++) {
                    if (isCancelled()) return null;
                    Thread.sleep(500);
                    publish(SAMPLE_DATA[i]);
                    int progress = (int) (((i + 1) * 100.0) / total);
                    setProgress(progress);
                }
                return null;
            }

            @Override
            protected void process(List<String[]> chunks) {
                for (String[] row : chunks) {
                    tableModel.addRow(row);
                }
            }

            @Override
            protected void done() {
                if (isCancelled()) {
                    lblStatus.setText("Đã hủy tải dữ liệu");
                } else {
                    lblStatus.setText("Tải xong " + tableModel.getRowCount() + " sản phẩm");
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
