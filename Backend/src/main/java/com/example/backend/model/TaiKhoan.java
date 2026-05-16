package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vai_tro", nullable = false)
    private VaiTro idVaiTro;

    @Nationalized
    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Nationalized
    @Column(name = "so_dien_thoai", nullable = false, length = 20)
    private String soDienThoai;

    @Nationalized
    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @Column(name = "ngay_cap_nhat")
    private Instant ngayCapNhat;

    @Nationalized
    @Column(name = "nguoi_tao", length = 100)
    private String nguoiTao;

    @Nationalized
    @Column(name = "nguoi_cap_nhat", length = 100)
    private String nguoiCapNhat;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Size(max = 100)
    @Column(name = "ten_tai_khoan", length = 100)
    private String tenTaiKhoan;

}