package com.example.backend.service;

import com.example.backend.response.TaiKhoanResponse;
import org.springframework.data.domain.Page;

public interface TaiKhoanService {
       Page<TaiKhoanResponse> phanTrangTaiKhoan(Integer pageNo, Integer pageSize);

}
