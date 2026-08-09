package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bai07MayTinhMini extends JFrame {
    private final JTextField txtDisplay = new JTextField();
    private final JTextArea txtHistory = new JTextArea(8, 20);
    private final List<String> history = new ArrayList<>();
    private String currentInput = "";
    private double storedValue = 0;
    private String operator = "";
    private boolean newNumber = true;

    public Bai07MayTinhMini() {
        setTitle("Bài 7 - Máy tính mini");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtDisplay.setEditable(false);
        txtDisplay.setFont(new Font("Monospaced", Font.BOLD, 20));
        txtDisplay.setHorizontalAlignment(JTextField.RIGHT);
        txtDisplay.setText("0");
        add(txtDisplay, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(5, 4, 3, 3));
        String[] btns = {
                "C", "CE", "←", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "±", "0", ".", "="
        };
        for (String b : btns) {
            JButton btn = new JButton(b);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.addActionListener(e -> xuLyNut(b));
            btnPanel.add(btn);
        }

        txtHistory.setEditable(false);
        JScrollPane scrollHistory = new JScrollPane(txtHistory);
        scrollHistory.setBorder(BorderFactory.createTitledBorder("Lịch sử"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, btnPanel, scrollHistory);
        split.setDividerLocation(280);
        add(split, BorderLayout.CENTER);

        setSize(520, 380);
        setLocationRelativeTo(null);
    }

    private void xuLyNut(String label) {
        switch (label) {
            case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> nhapSo(label);
            case "." -> nhapCham();
            case "+", "-", "×", "÷" -> datPhepTinh(label);
            case "=" -> ketQua();
            case "C" -> xoaAll();
            case "CE" -> xoaNhap();
            case "←" -> xoaMotKyTu();
            case "±" -> doiDau();
        }
    }

    private void nhapSo(String s) {
        if (newNumber) {
            currentInput = s;
            newNumber = false;
        } else {
            currentInput = currentInput + s;
        }
        hienThi(currentInput);
    }

    private void nhapCham() {
        if (newNumber) {
            currentInput = "0.";
            newNumber = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        hienThi(currentInput);
    }

    private void datPhepTinh(String op) {
        try {
            if (!operator.isEmpty() && !newNumber) {
                double current = Double.parseDouble(currentInput);
                double result = tinh(storedValue, current, operator);
                addHistory(storedValue + " " + operator + " " + current + " = " + formatDouble(result));
                storedValue = result;
                hienThi(formatDouble(result));
            } else {
                storedValue = Double.parseDouble(currentInput.isEmpty() ? "0" : currentInput);
            }
            operator = op;
            newNumber = true;
        } catch (ArithmeticException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            xoaAll();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        }
    }

    private void ketQua() {
        try {
            if (operator.isEmpty()) return;
            double current = Double.parseDouble(currentInput.isEmpty() ? "0" : currentInput);
            double result = tinh(storedValue, current, operator);
            String expr = formatDouble(storedValue) + " " + operator + " " + formatDouble(current) + " = " + formatDouble(result);
            addHistory(expr);
            hienThi(formatDouble(result));
            currentInput = formatDouble(result);
            operator = "";
            newNumber = true;
        } catch (ArithmeticException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            xoaAll();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        }
    }

    private double tinh(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "×" -> a * b;
            case "÷" -> {
                if (Math.abs(b) < 1e-12) throw new ArithmeticException("Lỗi: Chia cho 0!");
                yield a / b;
            }
            default -> 0;
        };
    }

    private void xoaAll() {
        currentInput = "";
        storedValue = 0;
        operator = "";
        newNumber = true;
        hienThi("0");
    }

    private void xoaNhap() {
        currentInput = "";
        newNumber = true;
        hienThi("0");
    }

    private void xoaMotKyTu() {
        if (!currentInput.isEmpty() && !newNumber) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            if (currentInput.isEmpty() || currentInput.equals("-")) {
                currentInput = "";
                newNumber = true;
                hienThi("0");
            } else {
                hienThi(currentInput);
            }
        }
    }

    private void doiDau() {
        if (newNumber || currentInput.isEmpty()) {
            return;
        }
        if (currentInput.startsWith("-")) {
            currentInput = currentInput.substring(1);
        } else {
            currentInput = "-" + currentInput;
        }
        hienThi(currentInput);
    }

    private void hienThi(String s) {
        txtDisplay.setText(s);
    }

    private String formatDouble(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d)) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }

    private void addHistory(String s) {
        history.add(0, s);
        StringBuilder sb = new StringBuilder();
        for (String item : history) {
            sb.append(item).append("\n");
        }
        txtHistory.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai07MayTinhMini().setVisible(true));
    }
}
