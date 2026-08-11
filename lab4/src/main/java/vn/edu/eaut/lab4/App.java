package vn.edu.eaut.lab4;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {
    private static final String[] MENU = {
        "1. Đếm ngược bằng SwingWorker",
        "2. Mô phỏng tải dữ liệu",
        "3. Tổng các số nguyên tố nhỏ hơn N",
        "4. Fibonacci bằng memoization",
        "5. Đọc file lớn và đếm số dòng",
        "6. Tổng số nguyên tố (có Hủy tác vụ)",
        "7. Tìm từ khóa trong file văn bản",
        "8. Đọc CSV điểm sinh viên & Thống kê",
        "9. Mô phỏng tải danh sách sản phẩm",
        "10. Quản lý sản phẩm bằng file CSV (Mini project)"
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showMenuLoop(args));
    }

    private static void showMenuLoop(String[] args) {
        String input = (String) JOptionPane.showInputDialog(
            null,
            "Chọn số bài tập muốn chạy (1-10):\n\n" + String.join("\n", MENU)
                + "\n\n(Để thoát, bấm Cancel hoặc đóng cửa sổ bài tập sau đó chọn Thoát)",
            "Lab 04 - SwingWorker - Chọn bài tập",
            JOptionPane.QUESTION_MESSAGE,
            null,
            null,
            "1"
        );

        if (input == null) {
            JOptionPane.showMessageDialog(null, "Tạm biệt! Hẹn gặp lại bạn lần sau.",
                "Thoát", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            return;
        }

        int choice;
        try {
            choice = Integer.parseInt(input.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                "Vui lòng nhập số nguyên từ 1 đến 10", "Lỗi", JOptionPane.ERROR_MESSAGE);
            showMenuLoop(args);
            return;
        }

        JFrame frame = createFrame(choice);
        if (frame == null) {
            JOptionPane.showMessageDialog(null,
                "Số không hợp lệ! Vui lòng chọn từ 1 đến 10", "Lỗi", JOptionPane.ERROR_MESSAGE);
            showMenuLoop(args);
            return;
        }

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                SwingUtilities.invokeLater(() -> showMenuLoop(args));
            }
        });

        frame.setVisible(true);
    }

    private static JFrame createFrame(int choice) {
        switch (choice) {
            case 1:  return new CountdownFrame();
            case 2:  return new ProgressDemoFrame();
            case 3:  return new PrimeSumFrame();
            case 4:  return new FibonacciFrame();
            case 5:  return new FileLineCounterFrame();
            case 6:  return new PrimeSumCancellableFrame();
            case 7:  return new FileSearchFrame();
            case 8:  return new StudentCSVFrame();
            case 9:  return new ProductLoadFrame();
            case 10: return new ProductManagerFrame();
            default: return null;
        }
    }
}
