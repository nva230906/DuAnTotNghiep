package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "khach_hang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan idTaiKhoan;

    @Nationalized
    @Column(name = "ma_khach_hang", nullable = false, length = 50)
    private String maKhachHang;

    @Nationalized
    @Column(name = "ho_ten", nullable = false, length = 150)
    private String hoTen;

    @Nationalized
    @Column(name = "so_dien_thoai", nullable = false, length = 20)
    private String soDienThoai;

    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Nationalized
    @Column(name = "hang_thanh_vien", length = 50)
    private String hangThanhVien;

    @ColumnDefault("0")
    @Column(name = "so_lan_mua")
    private Integer soLanMua;

    @Column(name = "ngay_mua_cuoi")
    private Instant ngayMuaCuoi;

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


    @Column(name = "trang_thai_xoa")
    private Integer trangThai;

    @Size(max = 255)
    @Nationalized
    @Column(name = "anh")
    private String anh;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Size(max = 255)
    @Nationalized
    @Column(name = "dia_chi")
    private String diaChi;

}