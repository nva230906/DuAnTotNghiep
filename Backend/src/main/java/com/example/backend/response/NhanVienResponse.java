package com.example.backend.response;

import com.example.backend.model.TaiKhoan;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NhanVienResponse {
    private Integer id;
    private Integer idTaiKhoan;
    private String email;
    private String soDienThoai;
    private Integer idVaiTro;
    private String tenVaiTro;
    private String maNhanVien;
    private String tenNhanVien;
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
