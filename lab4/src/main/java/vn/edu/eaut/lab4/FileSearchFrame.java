package vn.edu.eaut.lab4;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileSearchFrame extends JFrame {
    private JButton btnChooseFile;
    private JTextField txtKeyword;
    private JButton btnSearch;
    private JLabel lblFile;
    private JLabel lblCount;
    private JProgressBar progressBar;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private File selectedFile;
    private SwingWorker<List<String>, String> worker;

    public FileSearchFrame() {
        setTitle("Bài 7 - Tìm từ khóa trong file văn bản");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        btnChooseFile = new JButton("Chọn file .txt");
        txtKeyword = new JTextField(20);
        btnSearch = new JButton("Tìm kiếm");
        lblFile = new JLabel("File: Chưa chọn file");
        lblCount = new JLabel("Số dòng tìm thấy: 0");
        lblCount.setFont(new Font("Arial", Font.BOLD, 14));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPane = new JScrollPane(textArea);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(btnChooseFile);
        topPanel.add(new JLabel("Từ khóa: "));
        topPanel.add(txtKeyword);
        topPanel.add(btnSearch);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.add(lblFile);
        infoPanel.add(lblCount);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(progressBar, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnChooseFile.addActionListener(e -> chooseFile());
        btnSearch.addActionListener(e -> searchKeyword());
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Tệp văn bản (*.txt)", "txt"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            lblFile.setText("File: " + selectedFile.getAbsolutePath());
        }
    }

    private void searchKeyword() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file trước");
            return;
        }
        String keyword = txtKeyword.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa");
            return;
        }

        btnSearch.setEnabled(false);
        progressBar.setValue(0);
        textArea.setText("");
        lblCount.setText("Số dòng tìm thấy: Đang tìm...");

        final String lowerKeyword = keyword.toLowerCase();

        worker = new SwingWorker<List<String>, String>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                List<String> matches = new ArrayList<>();
                long totalBytes = Files.size(selectedFile.toPath());
                long readBytes = 0;
                int lineNumber = 0;
                try (BufferedReader reader = Files.newBufferedReader(
                        selectedFile.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (isCancelled()) return matches;
                        lineNumber++;
                        readBytes += line.getBytes(StandardCharsets.UTF_8).length + 1;
                        if (line.toLowerCase().contains(lowerKeyword)) {
                            String found = "Dòng " + lineNumber + ": " + line;
                            matches.add(found);
                            publish(found);
                        }
                        int progress = totalBytes == 0
                                ? 100
                                : (int) Math.min(100, (readBytes * 100 / totalBytes));
                        setProgress(progress);
                    }
                }
                return matches;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String chunk : chunks) {
                    textArea.append(chunk + "\n");
                }
            }

            @Override
            protected void done() {
                try {
                    List<String> result = get();
                    lblCount.setText("Số dòng tìm thấy: " + result.size());
                } catch (Exception ex) {
                    lblCount.setText("Lỗi khi tìm kiếm");
                }
                btnSearch.setEnabled(true);
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
