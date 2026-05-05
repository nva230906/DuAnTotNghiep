package com.example.backend.service.impl;

import com.example.backend.response.KhachHangResponse;
import com.example.backend.service.KhachHangServcie;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class KhachHangImpl implements KhachHangServcie {
    @Override
    public Page<KhachHangResponse> phanTrangKhachHang(Integer pageNo, Integer pageSize) {
        return null;
    }
}
