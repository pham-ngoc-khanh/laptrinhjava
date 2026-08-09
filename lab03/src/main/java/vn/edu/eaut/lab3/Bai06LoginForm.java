package vn.edu.eaut.lab3;

import javax.swing.*;
import java.awt.*;

public class Bai06LoginForm extends JFrame {
    private final JTextField txtUser = new JTextField();
    private final JPasswordField txtPass = new JPasswordField();
    private final JComboBox<String> cboRole;
    private final JCheckBox chkRemember = new JCheckBox("Nhớ tài khoản");
    private final JCheckBox chkShowPass = new JCheckBox("Hiển thị mật khẩu");

    public Bai06LoginForm() {
        setTitle("Bài 6 - Form đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formPanel.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Vai trò:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        String[] roles = {"Admin", "User", "Giảng viên", "Sinh viên", "Khách"};
        cboRole = new JComboBox<>(roles);
        formPanel.add(cboRole, gbc);

        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        checkPanel.add(chkRemember);
        checkPanel.add(chkShowPass);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(checkPanel, gbc);

        chkShowPass.addActionListener(e -> {
            if (chkShowPass.isSelected()) {
                txtPass.setEchoChar((char) 0);
            } else {
                txtPass.setEchoChar('\u2022');
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnLogin = new JButton("Đăng nhập");
        JButton btnReset = new JButton("Làm mới");
        JButton btnCancel = new JButton("Hủy");
        btnPanel.add(btnLogin);
        btnPanel.add(btnReset);
        btnPanel.add(btnCancel);

        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> dangNhap());
        btnReset.addActionListener(e -> lamMoi());
        btnCancel.addActionListener(e -> dispose());

        setSize(420, 260);
        setLocationRelativeTo(null);
    }

    private void dangNhap() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        String role = (String) cboRole.getSelectedItem();
        boolean remember = chkRemember.isSelected();

        if (user.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!");
            txtUser.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!");
            txtPass.requestFocus();
            return;
        }

        boolean isAdmin = "admin".equalsIgnoreCase(user) && "123456".equals(pass);
        boolean isUser = "user".equalsIgnoreCase(user) && "123456".equals(pass);
        boolean loginOk = isAdmin || isUser;

        if (loginOk) {
            String expectedRole = isAdmin ? "Admin" : "User";
            StringBuilder sb = new StringBuilder();
            sb.append("Đăng nhập thành công!\n\n");
            sb.append("Tài khoản: ").append(user).append("\n");
            sb.append("Vai trò mặc định: ").append(expectedRole).append("\n");
            sb.append("Vai trò đã chọn: ").append(role).append("\n");
            sb.append("Nhớ tài khoản: ").append(remember ? "Có" : "Không");
            JOptionPane.showMessageDialog(this, sb.toString(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!\n\n(Hint: admin/123456 hoặc user/123456)", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void lamMoi() {
        txtUser.setText("");
        txtPass.setText("");
        cboRole.setSelectedIndex(0);
        chkRemember.setSelected(false);
        txtUser.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai06LoginForm().setVisible(true));
    }
}
