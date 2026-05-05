package com.example.backend.request;

import com.example.backend.model.VaiTro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaiKhoanRequest {
    private Integer idVaiTro;
    private String email;
    private String soDienThoai;
    private String matKhau;
    private Instant ngayTao;
    private Instant ngayCapNhat;
    private String nguoiTao;
    private String nguoiCapNhat;
    private Integer trangThai;
}
