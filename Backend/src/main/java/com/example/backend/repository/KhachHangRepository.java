package com.example.backend.repository;

import com.example.backend.model.KhachHang;
import com.example.backend.response.KhachHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    @Query("""
                   SELECT NEW com.example.backend.response.KhachHangResponse(kh.id,kh.idTaiKhoan.id,kh.maKhachHang,kh.idTaiKhoan.email,kh.hoTen,kh.soDienThoai,kh.ngaySinh,kh.hangThanhVien,kh.soLanMua,kh.ngayMuaCuoi,kh.anh,kh.ngayTao,kh.ngayCapNhat,kh.nguoiTao,kh.nguoiCapNhat,kh.trangThai)
                   FROM KhachHang kh JOIN TaiKhoan tk on kh.idTaiKhoan.id=tk.id
            """)
    Page<KhachHangResponse> phanTrang(Pageable pageable);
}
