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
    private Integer idTaiKhoan;
    private String email;
    private String maKhachHang;
    private String hoTen;
    private String soDienThoai;
    private Date ngaySinh;
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
