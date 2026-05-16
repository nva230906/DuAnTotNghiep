package com.example.backend.request;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KhachHangRequest {
    //tài khoa
    private Integer idTaiKhoan;
    private String email;
    private String tenTaiKhoan;
    private String soDienThoai;
    //khach hang
    private String maKhachHang;
    private String hoTen;
    private Date ngaySinh;
    private String anh;
    private String gioiTinh;
    private String diaChi;
    private Integer trangThai;

    //địa chỉ
    private String thanhPho;
    private String quan;
    private String phuong;
    private String diaChiCuThe;
}
