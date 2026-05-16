package com.example.backend.repository;

import com.example.backend.model.TaiKhoan;
import com.example.backend.response.TaiKhoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    @Query("""
                SELECT NEW com.example.backend.response.TaiKhoanResponse(tk.id,tk.idVaiTro.id,tk.idVaiTro.tenVaiTro,tk.email,tk.soDienThoai,tk.matKhau,tk.ngayTao,tk.ngayCapNhat,tk.nguoiTao,tk.nguoiCapNhat,tk.trangThai)
                FROM TaiKhoan tk JOIN VaiTro vt on tk.idVaiTro.id=vt.id
            """)
    Page<TaiKhoanResponse> phanTrang(Pageable pageable);

    Optional<TaiKhoan> findByEmail(String email);
}
