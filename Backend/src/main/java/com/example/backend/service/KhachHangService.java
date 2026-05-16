package com.example.backend.service;

import com.example.backend.request.KhachHangRequest;
import com.example.backend.response.KhachHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface KhachHangService {
    Page<KhachHangResponse> phanTrangKhachHang(Integer pageNo, Integer pageSize);

    void addKhachHang(KhachHangRequest khachHangRequest, MultipartFile file);

    void updateKhachHang(Integer id, KhachHangRequest khachHangRequest, MultipartFile file);

    List<KhachHangResponse> searchFullKhachHang(String keyword);

    KhachHangResponse detailKhachHang(Integer id);

    ByteArrayInputStream khachHangExcel(String keyword, Boolean gender, Integer status);

}
