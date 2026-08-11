package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;

public class PrimeSumFrame extends JFrame {
    private JTextField txtN;
    private JButton btnCalculate;
    private JLabel lblResult;
    private JProgressBar progressBar;

    public PrimeSumFrame() {
        setTitle("Bài 3 - Tổng các số nguyên tố nhỏ hơn N");
        setSize(450, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        txtN = new JTextField(10);
        btnCalculate = new JButton("Tính");
        lblResult = new JLabel("Kết quả: ");
        lblResult.setFont(new Font("Arial", Font.BOLD, 14));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Nhập N: "));
        inputPanel.add(txtN);
        inputPanel.add(btnCalculate);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.add(inputPanel);
        panel.add(progressBar);
        panel.add(lblResult);
        add(panel);

        btnCalculate.addActionListener(e -> calculatePrimeSum());
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private void calculatePrimeSum() {
        int n;
        try {
            n = Integer.parseInt(txtN.getText().trim());
            if (n <= 2) {
                JOptionPane.showMessageDialog(this, "N phải lớn hơn 2");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ");
            return;
        }

        btnCalculate.setEnabled(false);
        progressBar.setValue(0);
        lblResult.setText("Đang tính...");

        SwingWorker<Long, Void> worker = new SwingWorker<Long, Void>() {
            @Override
            protected Long doInBackground() {
                long sum = 0;
                for (int i = 2; i < n; i++) {
                    if (isPrime(i)) sum += i;
                    int progress = (int) ((i * 100.0) / n);
                    setProgress(progress);
                }
                return sum;
            }

            @Override
            protected void done() {
                try {
                    long result = get();
                    lblResult.setText("Tổng các số nguyên tố nhỏ hơn " + n + " = " + result);
                } catch (Exception ex) {
                    lblResult.setText("Có lỗi khi tính toán");
                }
                btnCalculate.setEnabled(true);
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
