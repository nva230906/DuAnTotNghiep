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
	    ten_tai_khoan VARCHAR(100),

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
CREATE TABLE ca_lam_viec (
    id            BIGINT IDENTITY(1,1) PRIMARY KEY,
    ma_ca         NVARCHAR(50)  NOT NULL UNIQUE,
    ten_ca        NVARCHAR(100) NOT NULL,
    gio_bat_dau   TIME          NOT NULL,
    gio_ket_thuc  TIME          NOT NULL,
    so_gio        INT           NOT NULL,       -- số giờ làm thực tế
    mo_ta         NVARCHAR(255),
    ngay_tao      DATETIME      DEFAULT GETDATE(),
    nguoi_tao     NVARCHAR(100),
    trang_thai    INT           DEFAULT 1
);

-- Bảng phân ca (lịch làm từng ngày của từng nhân viên)
CREATE TABLE phan_ca (
    id               BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_nhan_vien     BIGINT        NOT NULL,
    id_ca_lam_viec   BIGINT        NOT NULL,
    ngay_lam         DATE          NOT NULL,
    ghi_chu          NVARCHAR(255),
    nguoi_tao        NVARCHAR(100),
    nguoi_cap_nhat   NVARCHAR(100),
    ngay_tao         DATETIME      DEFAULT GETDATE(),
    ngay_cap_nhat    DATETIME,
    trang_thai       INT           DEFAULT 1,  -- 1: active, 0: đã huỷ
    CONSTRAINT FK_phan_ca_nhan_vien
        FOREIGN KEY (id_nhan_vien)   REFERENCES nhan_vien(id),
    CONSTRAINT FK_phan_ca_ca_lam_viec
        FOREIGN KEY (id_ca_lam_viec) REFERENCES ca_lam_viec(id),
    CONSTRAINT UQ_phan_ca_nv_ngay
        UNIQUE (id_nhan_vien, ngay_lam)         -- 1 NV chỉ 1 ca/ngày
);

-- Bảng chấm công (ghi nhận giờ vào/ra thực tế)
CREATE TABLE cham_cong (
    id                 BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_phan_ca         BIGINT        NOT NULL UNIQUE,  -- 1 phân ca = 1 bản ghi
    gio_vao_thuc_te    DATETIME,
    gio_ra_thuc_te     DATETIME,
    so_phut_di_tre     INT           DEFAULT 0,
    so_phut_ve_som     INT           DEFAULT 0,
    hinh_thuc          NVARCHAR(50),  -- 'app', 'van_tay', 'qr', 'thu_cong'
    anh_check_in       NVARCHAR(255),
    ghi_chu            NVARCHAR(255),
    nguoi_cap_nhat     NVARCHAR(100),
    ngay_cap_nhat      DATETIME,
    trang_thai         INT           DEFAULT 1,  -- 1: đúng giờ, 2: trễ, 3: về sớm, 4: vắng
    CONSTRAINT FK_cham_cong_phan_ca
        FOREIGN KEY (id_phan_ca) REFERENCES phan_ca(id)
);
CREATE TABLE khach_hang (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_tai_khoan BIGINT UNIQUE, 
    ma_khach_hang NVARCHAR(50) NOT NULL,
    ho_ten NVARCHAR(150) NOT NULL,
    so_dien_thoai NVARCHAR(20) NOT NULL UNIQUE, 
    ngay_sinh DATE,
    hang_thanh_vien NVARCHAR(50),
	dia_chi NVARCHAR(255),
    so_lan_mua INT DEFAULT 0,
    ngay_mua_cuoi DATETIME,
	anh NVARCHAR(255),
	gioi_tinh BIT,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME,
    nguoi_tao NVARCHAR(100),
    nguoi_cap_nhat NVARCHAR(100),
    trang_thai_xoa BIT DEFAULT 0,
    CONSTRAINT FK_khach_hang_tai_khoan
    FOREIGN KEY (id_tai_khoan)
    REFERENCES tai_khoan(id)
);
CREATE TABLE dia_chi_khach_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_dia_chi VARCHAR(50),
    ten_nguoi_nhan NVARCHAR(255),
    so_dien_thoai VARCHAR(15),
    thanh_pho NVARCHAR(100),
    quan NVARCHAR(100),
    phuong NVARCHAR(100),
    dia_chi_cu_the NVARCHAR(MAX),
    mac_dinh BIT,
    trang_thai INT,
    id_khach_hang BIGINT,
    CONSTRAINT FK_dia_chi_khach_hang
    FOREIGN KEY (id_khach_hang)
    REFERENCES khach_hang(id)
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

-- KHACH_HANG
INSERT INTO khach_hang (id_tai_khoan, ma_khach_hang, ho_ten, so_dien_thoai, ngay_sinh, hang_thanh_vien, dia_chi, so_lan_mua, ngay_mua_cuoi, anh, gioi_tinh, nguoi_tao, trang_thai_xoa)
VALUES
(4, 'KH001', N'Nguyễn Văn An', '0911111111', '2000-05-12', N'Bạc', N'123 Lê Lợi, Hà Nội', 5, GETDATE(), 'an.jpg', 1, 'system', 0),
(5, 'KH002', N'Trần Thị Bình', '0922222222', '1998-09-20', N'Vàng', N'456 Nguyễn Trãi, Hà Nội', 12, GETDATE(), 'binh.jpg', 0, 'system', 0);
USE master;
GO

ALTER DATABASE DATN SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO

DROP DATABASE DATN;
GO
select*from tai_khoan
select*from khach_hang
select*from nhan_vien
select*from vai_tro