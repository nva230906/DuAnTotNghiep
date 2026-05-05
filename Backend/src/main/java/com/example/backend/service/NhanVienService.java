package com.example.backend.service;

import com.example.backend.model.NhanVien;
import com.example.backend.request.NhanVienRequest;
import com.example.backend.response.NhanVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface NhanVienService {
    Page<NhanVienResponse> phanTrangNhanVien(Integer pageNo, Integer pageSize);

    void deleteNhanVien(Integer id);

    void addNhanVien(NhanVienRequest nhanVienRequest, MultipartFile file);

    void updateNhanVien(Integer id,NhanVienRequest nhanVienRequest, MultipartFile file);

    List<NhanVienResponse> searchFullNhanVien(String keyword);

    NhanVienResponse detailNhanVien(Integer id);

    ByteArrayInputStream nhanVienExcel(String keyword, Boolean gender, Integer status);
}
