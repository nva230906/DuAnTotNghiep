package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "dia_chi_khach_hang")
public class DiaChiKhachHang {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @Column(name = "ma_dia_chi", length = 50)
    private String maDiaChi;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ten_nguoi_nhan")
    private String tenNguoiNhan;

    @Size(max = 15)
    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    @Size(max = 100)
    @Nationalized
    @Column(name = "thanh_pho", length = 100)
    private String thanhPho;

    @Size(max = 100)
    @Nationalized
    @Column(name = "quan", length = 100)
    private String quan;

    @Size(max = 100)
    @Nationalized
    @Column(name = "phuong", length = 100)
    private String phuong;

    @Nationalized
    @Lob
    @Column(name = "dia_chi_cu_the")
    private String diaChiCuThe;

    @Column(name = "mac_dinh")
    private Boolean macDinh;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

}