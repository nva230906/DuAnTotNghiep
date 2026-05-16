package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "ca_lam_viec")
public class CaLamViec {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "ma_ca", nullable = false, length = 50)
    private String maCa;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "ten_ca", nullable = false, length = 100)
    private String tenCa;

    @NotNull
    @Column(name = "gio_bat_dau", nullable = false)
    private LocalTime gioBatDau;

    @NotNull
    @Column(name = "gio_ket_thuc", nullable = false)
    private LocalTime gioKetThuc;

    @NotNull
    @Column(name = "so_gio", nullable = false)
    private Integer soGio;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @Size(max = 100)
    @Nationalized
    @Column(name = "nguoi_tao", length = 100)
    private String nguoiTao;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Integer trangThai;

}