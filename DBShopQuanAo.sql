create DATABASE DATN;
GO
USE DATN;
GO

CREATE TABLE vai_tro (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
	ma_vai_tro NVARCHAR(50) NOT NULL,
    ten_vai_tro NVARCHAR(100) NOT NULL,
    trang_thai INT
);
CREATE TABLE tai_khoan (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_vai_tro BIGINT NOT NULL,
    email NVARCHAR(150) NOT NULL UNIQUE,
    so_dien_thoai NVARCHAR(20) NOT NULL UNIQUE,
    mat_khau NVARCHAR(255) NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME,
    nguoi_tao NVARCHAR(100),
    nguoi_cap_nhat NVARCHAR(100),
    trang_thai INT,
    CONSTRAINT FK_tai_khoan_vai_tro
    FOREIGN KEY (id_vai_tro)
    REFERENCES vai_tro(id)
);
CREATE TABLE nhan_vien (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_tai_khoan BIGINT NOT NULL UNIQUE,
    ma_nhan_vien NVARCHAR(50) NOT NULL,
    ten_nhan_vien NVARCHAR(150) NOT NULL,
    dia_chi NVARCHAR(255),
    ngay_sinh DATE,
    gioi_tinh BIT,
    can_cuoc_cong_dan NVARCHAR(20) UNIQUE, 
    anh NVARCHAR(255),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME,
    nguoi_tao NVARCHAR(100),
    nguoi_cap_nhat NVARCHAR(100),
    trang_thai INT,
    CONSTRAINT FK_nhan_vien_tai_khoan
    FOREIGN KEY (id_tai_khoan)
    REFERENCES tai_khoan(id)
);
CREATE TABLE khach_hang (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_tai_khoan BIGINT UNIQUE, 
    ma_khach_hang NVARCHAR(50) NOT NULL,
    ho_ten NVARCHAR(150) NOT NULL,
    so_dien_thoai NVARCHAR(20) NOT NULL UNIQUE, 
    ngay_sinh DATE,
    hang_thanh_vien NVARCHAR(50),
    so_lan_mua INT DEFAULT 0,
    ngay_mua_cuoi DATETIME,
	anh NVARCHAR(255),
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME,
    nguoi_tao NVARCHAR(100),
    nguoi_cap_nhat NVARCHAR(100),
    trang_thai_xoa BIT DEFAULT 0,
    CONSTRAINT FK_khach_hang_tai_khoan
    FOREIGN KEY (id_tai_khoan)
    REFERENCES tai_khoan(id)
);
INSERT INTO vai_tro (ma_vai_tro, ten_vai_tro, trang_thai) VALUES
('QL', N'Quản lý', 1),
('NV', N'Nhân viên', 1),
('KH', N'Khách hàng', 1);
INSERT INTO tai_khoan (
    id_vai_tro, email, so_dien_thoai, mat_khau, nguoi_tao, trang_thai
) VALUES
-- Quản lý
(1, 'quanly@shop.com', '0901000001', '123456', 'system', 1),

-- Nhân viên
(2, 'nhanvien01@shop.com', '0901000002', '123456', 'quanly', 1),
(2, 'nhanvien02@shop.com', '0901000003', '123456', 'quanly', 1),

-- Khách hàng có tài khoản
(3, 'khach01@gmail.com', '0901000004', '123456', 'system', 1),
(3, 'khach02@gmail.com', '0901000005', '123456', 'system', 1);
INSERT INTO nhan_vien (
    id_tai_khoan,
    ma_nhan_vien,
    ten_nhan_vien,
    dia_chi,
    ngay_sinh,
    gioi_tinh,
    can_cuoc_cong_dan,
    anh,
    nguoi_tao,
    trang_thai
)
VALUES
-- Quản lý
(1, 'NV001', N'Nguyễn Văn Quản Lý', N'Hà Nội', '1990-01-01', 1, '001090000001', 'ql.jpg', 'system', 1),

-- Nhân viên
(2, 'NV002', N'Trần Thị Lan', N'Hồ Chí Minh', '1998-05-10', 0, '001098000002', 'lan.jpg', 'quanly', 1),
(3, 'NV003', N'Lê Văn Nam', N'Đà Nẵng', '1997-08-20', 1, '001097000003', 'nam.jpg', 'quanly', 1);

USE master;
GO

ALTER DATABASE DATN SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO

DROP DATABASE DATN;
GO
select*from nhan_vien