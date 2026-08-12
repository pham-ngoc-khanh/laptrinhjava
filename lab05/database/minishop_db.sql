CREATE DATABASE minishop_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE minishop_db;

CREATE TABLE san_pham (
    ma_sp INT AUTO_INCREMENT PRIMARY KEY,
    ten_sp VARCHAR(100) NOT NULL,
    don_gia DECIMAL(12,2) NOT NULL,
    so_luong INT NOT NULL DEFAULT 0
);

CREATE TABLE khach_hang (
    ma_kh INT AUTO_INCREMENT PRIMARY KEY,
    ten_kh VARCHAR(100) NOT NULL,
    sdt VARCHAR(18) NOT NULL,
    dia_chi VARCHAR(255)
);

CREATE TABLE hoa_don (
    ma_hd INT AUTO_INCREMENT PRIMARY KEY,
    ngay_lap DATE NOT NULL,
    ma_kh INT NOT NULL,
    tong_tien DECIMAL(12,2) DEFAULT 0,
    FOREIGN KEY (ma_kh) REFERENCES khach_hang(ma_kh)
);

CREATE TABLE chi_tiet_hoa_don (
    ma_hd INT NOT NULL,
    ma_sp INT NOT NULL,
    so_luong INT NOT NULL,
    don_gia DECIMAL(12,2) NOT NULL,
    thanh_tien DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (ma_hd, ma_sp),
    FOREIGN KEY (ma_hd) REFERENCES hoa_don(ma_hd),
    FOREIGN KEY (ma_sp) REFERENCES san_pham(ma_sp)
);

INSERT INTO san_pham(ten_sp, don_gia, so_luong) VALUES
('Ban phim Logitech K120', 180000, 50),
('Chuot khong day Rapoo', 220000, 40),
('USB Kingston 32GB', 150000, 100),
('Tai nghe Sony Basic', 350000, 30);

INSERT INTO khach_hang(ten_kh, sdt, dia_chi) VALUES
('Nguyen Van An', '0912345678', 'Ha Noi'),
('Tran Thi Binh', '0987654321', 'Bac Ninh'),
('Le Van Cuong', '0901111222', 'Hai Duong');
