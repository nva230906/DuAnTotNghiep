package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "nhan_vien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tai_khoan", nullable = false)
    private TaiKhoan idTaiKhoan;

    @Nationalized
    @Column(name = "ma_nhan_vien", nullable = false, length = 50)
    private String maNhanVien;

    @Nationalized
    @Column(name = "ten_nhan_vien", nullable = false, length = 150)
    private String tenNhanVien;

    @Nationalized
    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Nationalized
    @Column(name = "can_cuoc_cong_dan", length = 20)
    private String canCuocCongDan;

    @Nationalized
    @Column(name = "anh")
    private String anh;

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


}