package com.example.backend.service.impl;

import com.example.backend.response.TaiKhoanResponse;
import com.example.backend.service.TaiKhoanService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TaiKhoanImpl implements TaiKhoanService {
    @Override
    public Page<TaiKhoanResponse> phanTrangTaiKhoan(Integer pageNo, Integer pageSize) {
        return null;
    }
}
