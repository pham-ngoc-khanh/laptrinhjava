package vn.edu.eaut.lab5.ui;

import vn.edu.eaut.lab5.config.DBHelper;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final SanPhamPanel sanPhamPanel = new SanPhamPanel();
    private final KhachHangPanel khachHangPanel = new KhachHangPanel();
    private final HoaDonPanel hoaDonPanel = new HoaDonPanel();
    private final ThongKePanel thongKePanel = new ThongKePanel();

    public MainFrame() {
        initFrame();
        initMenu();
        initTabs();
        checkConnection();
    }

    private void initFrame() {
        setTitle("MiniShop - Quản lý bán hàng");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuHeThong = new JMenu("Hệ thống");
        JMenuItem itemKetNoi = new JMenuItem("Kiểm tra kết nối");
        JMenuItem itemThoat = new JMenuItem("Thoát");
        menuHeThong.add(itemKetNoi);
        menuHeThong.addSeparator();
        menuHeThong.add(itemThoat);
        menuBar.add(menuHeThong);

        JMenu menuChucNang = new JMenu("Chức năng");
        JMenuItem itemSanPham = new JMenuItem("Quản lý sản phẩm");
        JMenuItem itemKhachHang = new JMenuItem("Quản lý khách hàng");
        JMenuItem itemHoaDon = new JMenuItem("Lập hóa đơn");
        JMenuItem itemThongKe = new JMenuItem("Thống kê");
        menuChucNang.add(itemSanPham);
        menuChucNang.add(itemKhachHang);
        menuChucNang.add(itemHoaDon);
        menuChucNang.add(itemThongKe);
        menuBar.add(menuChucNang);

        setJMenuBar(menuBar);

        JTabbedPane tabs = new JTabbedPane();
        itemSanPham.addActionListener(e -> tabs.setSelectedIndex(0));
        itemKhachHang.addActionListener(e -> tabs.setSelectedIndex(1));
        itemHoaDon.addActionListener(e -> tabs.setSelectedIndex(2));
        itemThongKe.addActionListener(e -> tabs.setSelectedIndex(3));
        itemKetNoi.addActionListener(e -> checkConnection());
        itemThoat.addActionListener(e -> System.exit(0));
    }

    private void initTabs() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab("🛍️ Sản phẩm", sanPhamPanel);
        tabbedPane.addTab("👥 Khách hàng", khachHangPanel);
        tabbedPane.addTab("🧾 Hóa đơn", hoaDonPanel);
        tabbedPane.addTab("📊 Thống kê", thongKePanel);
        add(tabbedPane, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblStatus = new JLabel("💻 MiniShop - Lab 5 | Kết nối: localhost:3306/minishop_db");
        statusBar.add(lblStatus);
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);
    }

    private void checkConnection() {
        try {
            if (DBHelper.getConnection() != null) {
                JOptionPane.showMessageDialog(this,
                        "✅ Kết nối cơ sở dữ liệu thành công!\nMySQL: localhost:3306/minishop_db",
                        "Trạng thái kết nối", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Kết nối thất bại: " + e.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }
}
