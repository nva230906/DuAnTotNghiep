package com.example.backend.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VaiTroResponse {
    private Integer id;
    private String maVaiTro;
    private String tenVaiTro;
    private Integer trangThai;
}
