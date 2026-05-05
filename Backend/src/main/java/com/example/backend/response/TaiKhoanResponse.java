package com.example.backend.response;

import com.example.backend.model.VaiTro;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaiKhoanResponse {
    private Integer id;
    private Integer idVaiTro;
    private String tenVaiTro;
    private String email;
    private String soDienThoai;
    private String matKhau;
    private Instant ngayTao;
    private Instant ngayCapNhat;
    private String nguoiTao;
    private String nguoiCapNhat;
    private Integer trangThai;
}
