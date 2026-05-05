package com.example.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NhanVienRequest {
    private Integer idTaiKhoan;
    private String email;
    private String soDienThoai;
    private Integer idVaiTro;
    private String tenVaiTro;
    private String matKhau;
    private String maNhanVien;
    private String tenNhanVien;
     private String tinhThanhPho;
    private String quanHuyen;
    private String phuongXa;
    private String diaChi;
    private LocalDate ngaySinh;
    private Boolean gioiTinh;
    private String canCuocCongDan;
    private String anh;
    private Instant ngayTao;
    private Instant ngayCapNhat;
    private String nguoiTao;
    private String nguoiCapNhat;
    private Integer trangThai;

}
