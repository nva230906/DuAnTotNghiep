package com.example.backend.service;

import com.example.backend.response.KhachHangResponse;
import org.springframework.data.domain.Page;

public interface KhachHangServcie {
       Page<KhachHangResponse> phanTrangKhachHang(Integer pageNo, Integer pageSize);

}
