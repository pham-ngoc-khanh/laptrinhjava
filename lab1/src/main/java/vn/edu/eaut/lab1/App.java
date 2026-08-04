package vn.edu.eaut.lab1;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int choice;

            do {
                inMenu();
                while (!scanner.hasNextInt()) {
                    System.out.print("Vui long nhap so nguyen cho menu: ");
                    scanner.nextLine();
                }

                choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> bai1(scanner);
                    case 2 -> bai2(scanner);
                    case 3 -> bai3(scanner);
                    case 4 -> bai4(scanner);
                    case 5 -> bai5(scanner);
                    case 0 -> System.out.println("Ket thuc chuong trinh.");
                    default -> System.out.println("Lua chon khong hop le.");
                }
            } while (choice != 0);
        }
    }

    private static void inMenu() {
        System.out.println("\n================ LAB 1 - JAVA CONSOLE ================");
        System.out.println("1. Tinh tong so chan nho hon n");
        System.out.println("2. Tinh tong nghich dao");
        System.out.println("3. Kiem tra so nguyen to");
        System.out.println("4. Kiem tra va phan loai tam giac");
        System.out.println("5. Hien thi day Fibonacci");
        System.out.println("0. Thoat");
        System.out.print("Moi chon chuc nang: ");
    }

    private static void bai1(Scanner scanner) {
        System.out.print("Nhap n = ");
        int n = scanner.nextInt();
        System.out.printf("Tong so chan nho hon %d la: %d%n", n, So.tinhTongSoChanNhoHonN(n));
    }

    private static void bai2(Scanner scanner) {
        System.out.print("Nhap n = ");
        int n = scanner.nextInt();

        try {
            System.out.printf("S = 1 + 1/2 + ... + 1/%d = %.4f%n", n, So.tinhTongNghichDao(n));
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void bai3(Scanner scanner) {
        System.out.print("Nhap n = ");
        int n = scanner.nextInt();

        if (So.kiemTraSoNguyenTo(n)) {
            System.out.printf("%d la so nguyen to.%n", n);
        } else {
            System.out.printf("%d khong phai la so nguyen to.%n", n);
        }
    }

    private static void bai4(Scanner scanner) {
        System.out.print("Nhap a = ");
        double a = scanner.nextDouble();
        System.out.print("Nhap b = ");
        double b = scanner.nextDouble();
        System.out.print("Nhap c = ");
        double c = scanner.nextDouble();

        System.out.printf("Ket qua: %s%n", So.xepLoaiTamGiac(a, b, c));
    }

    private static void bai5(Scanner scanner) {
        System.out.print("Nhap so luong phan tu n = ");
        int n = scanner.nextInt();

        try {
            System.out.printf("Day Fibonacci: %s%n", So.dayFibonacci(n));
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
