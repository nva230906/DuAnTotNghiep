package com.example.backend.service;

import com.example.backend.response.VaiTroResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VaiTroService {
     Page<VaiTroResponse> phanTrangVaiTro(Integer pageNo, Integer pageSize);
     List<VaiTroResponse> getAllVaiTro();
}
