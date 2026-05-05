package com.example.backend.service.impl;

import com.example.backend.repository.VaiTroRepository;
import com.example.backend.response.VaiTroResponse;
import com.example.backend.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaiTroImpl implements VaiTroService {
    @Autowired
    VaiTroRepository vaiTroRepository;
    @Override
    public Page<VaiTroResponse> phanTrangVaiTro(Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public List<VaiTroResponse> getAllVaiTro() {
        return vaiTroRepository.hienThi();
    }
}
