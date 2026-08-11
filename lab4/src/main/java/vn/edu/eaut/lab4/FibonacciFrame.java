package vn.edu.eaut.lab4;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class FibonacciFrame extends JFrame {
    private JTextField txtN;
    private JButton btnFind;
    private JLabel lblResult;
    private JProgressBar progressBar;

    public FibonacciFrame() {
        setTitle("Bài 4 - Fibonacci bằng memoization");
        setSize(500, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        txtN = new JTextField(10);
        btnFind = new JButton("Tìm");
        lblResult = new JLabel("Kết quả: ");
        lblResult.setFont(new Font("Arial", Font.BOLD, 12));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Nhập N: "));
        inputPanel.add(txtN);
        inputPanel.add(btnFind);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.add(inputPanel);
        panel.add(progressBar);
        panel.add(lblResult);
        add(panel);

        btnFind.addActionListener(e -> findFibonacci());
    }

    private BigInteger fibonacci(int n, Map<Integer, BigInteger> memo) {
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }
        if (memo.containsKey(n)) {
            return memo.get(n);
        }
        BigInteger value = fibonacci(n - 1, memo).add(fibonacci(n - 2, memo));
        memo.put(n, value);
        return value;
    }

    private void findFibonacci() {
        int n;
        try {
            n = Integer.parseInt(txtN.getText().trim());
            if (n < 0) {
                JOptionPane.showMessageDialog(this, "N phải >= 0");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ");
            return;
        }

        btnFind.setEnabled(false);
        progressBar.setIndeterminate(true);
        lblResult.setText("Đang tính Fibonacci...");

        SwingWorker<BigInteger, Void> worker = new SwingWorker<BigInteger, Void>() {
            @Override
            protected BigInteger doInBackground() {
                Map<Integer, BigInteger> memo = new HashMap<>();
                return fibonacci(n, memo);
            }

            @Override
            protected void done() {
                try {
                    BigInteger result = get();
                    lblResult.setText("Fibonacci(" + n + ") = " + result);
                } catch (Exception ex) {
                    lblResult.setText("Có lỗi khi tính Fibonacci");
                }
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                btnFind.setEnabled(true);
            }
        };

        worker.execute();
    }
}
