package com.example.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaLamViecRequest {
    private String maCa;
    private String tenCa;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private Integer soGio;
    private String moTa;
    private Instant ngayTao;
    private String nguoiTao;
    private Integer trangThai;
}
