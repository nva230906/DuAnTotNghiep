package com.example.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KhachHangRequest {
    private Integer idTaiKhoan;
    private String maKhachHang;
    private String hoTen;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private String hangThanhVien;
    private Integer soLanMua;
    private Instant ngayMuaCuoi;
    private String anh;
    private Instant ngayTao;
    private Instant ngayCapNhat;
    private String nguoiTao;
    private String nguoiCapNhat;
    private Integer trangThai;
}
