package com.example.backend.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KhachHangResponse {
    private Integer id;
    private String maKhachHang;
    private String hoTen;
    private Integer idTaiKhoan;
    private String email;
    private String tenTaiKhoan;
    private String soDienThoai;
    private Date ngaySinh;
    private String anh;
    private String thanhPho;
    private String quan;
    private String phuong;
    private String diaChiCuThe;
    private String diaChi;
    private Boolean gioTinh;
    private Integer trangThai;
}
