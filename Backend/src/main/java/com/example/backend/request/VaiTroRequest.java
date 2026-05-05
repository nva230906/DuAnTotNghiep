package com.example.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VaiTroRequest {
    private String maVaiTro;
    private String tenVaiTro;
    private Integer trangThai;
}
