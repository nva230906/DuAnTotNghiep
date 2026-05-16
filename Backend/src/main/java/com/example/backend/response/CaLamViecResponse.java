package com.example.backend.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaLamViecResponse {
    private Integer id;
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
