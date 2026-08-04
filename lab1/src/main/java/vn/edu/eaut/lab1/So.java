package vn.edu.eaut.lab1;

public final class So {

    private So() {
    }

    public static int tinhTongSoChanNhoHonN(int n) {
        int tong = 0;
        for (int i = 0; i < n; i += 2) {
            tong += i;
        }
        return tong;
    }

    public static double tinhTongNghichDao(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n phai lon hon 0");
        }

        double tong = 0;
        for (int i = 1; i <= n; i++) {
            tong += 1.0 / i;
        }
        return tong;
    }

    public static boolean kiemTraSoNguyenTo(int n) {
        if (n < 2) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }

        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static String xepLoaiTamGiac(double a, double b, double c) {
        if (a <= 0 || b <= 0 || c <= 0) {
            return "Khong phai tam giac";
        }
        if (a + b <= c || a + c <= b || b + c <= a) {
            return "Khong phai tam giac";
        }

        double epsilon = 1e-9;
        boolean can = Math.abs(a - b) < epsilon
                || Math.abs(a - c) < epsilon
                || Math.abs(b - c) < epsilon;
        boolean deu = Math.abs(a - b) < epsilon && Math.abs(b - c) < epsilon;
        boolean vuong = Math.abs(a * a + b * b - c * c) < epsilon
                || Math.abs(a * a + c * c - b * b) < epsilon
                || Math.abs(b * b + c * c - a * a) < epsilon;

        if (deu) {
            return "Tam giac deu";
        }
        if (vuong && can) {
            return "Tam giac vuong can";
        }
        if (vuong) {
            return "Tam giac vuong";
        }
        if (can) {
            return "Tam giac can";
        }
        return "Tam giac thuong";
    }

    public static String dayFibonacci(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n phai la so nguyen duong");
        }

        StringBuilder ketQua = new StringBuilder();
        long a = 0;
        long b = 1;

        for (int i = 1; i <= n; i++) {
            ketQua.append(a);
            if (i < n) {
                ketQua.append(' ');
            }

            long temp = a + b;
            a = b;
            b = temp;
        }

        return ketQua.toString();
    }
}
