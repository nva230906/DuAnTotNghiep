package com.example.backend.repository;

import com.example.backend.model.KhachHang;
import com.example.backend.response.KhachHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer>, JpaSpecificationExecutor<KhachHang> {
    @Query("""
                SELECT NEW com.example.backend.response.KhachHangResponse(kh.id,kh.maKhachHang,kh.hoTen,tk.id,tk.email,tk.tenTaiKhoan,tk.soDienThoai,kh.ngaySinh,kh.anh,dc.thanhPho,dc.quan,dc.phuong,dc.diaChiCuThe,kh.diaChi,kh.gioiTinh,kh.trangThai)
                FROM KhachHang kh
                LEFT JOIN kh.idTaiKhoan tk
                LEFT JOIN DiaChiKhachHang dc
                ON dc.idKhachHang.id = kh.id
            """)
    Page<KhachHangResponse> phanTrang(Pageable pageable);
    @Query("""
                SELECT NEW com.example.backend.response.KhachHangResponse(kh.id,kh.maKhachHang,kh.hoTen,tk.id,tk.email,tk.tenTaiKhoan,tk.soDienThoai,kh.ngaySinh,kh.anh,dc.thanhPho,dc.quan,dc.phuong,dc.diaChiCuThe,kh.diaChi,kh.gioiTinh,kh.trangThai)
                FROM KhachHang kh
                LEFT JOIN kh.idTaiKhoan tk
                LEFT JOIN DiaChiKhachHang dc
                ON dc.idKhachHang.id = kh.id
                WHERE tk.id=?1
            """)
    KhachHangResponse detail(Integer id);
    @Query("""
            SELECT NEW com.example.backend.response.KhachHangResponse(
                kh.id,
                kh.maKhachHang,
                kh.hoTen,
                tk.id,
                tk.email,
                tk.tenTaiKhoan,
                tk.soDienThoai,
                kh.ngaySinh,
                kh.anh,
                dc.thanhPho,
                dc.quan,
                dc.phuong,
                dc.diaChiCuThe,
                kh.diaChi,
                kh.gioiTinh,
                kh.trangThai
            )
            FROM KhachHang kh
            LEFT JOIN kh.idTaiKhoan tk
            LEFT JOIN DiaChiKhachHang dc
                ON dc.idKhachHang.id = kh.id
            WHERE
                LOWER(kh.maKhachHang) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(kh.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(tk.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(tk.tenTaiKhoan) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(tk.soDienThoai) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(dc.thanhPho) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(dc.quan) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(dc.phuong) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR dc.diaChiCuThe LIKE CONCAT('%', :keyword, '%')
                OR kh.diaChi LIKE CONCAT('%', :keyword, '%')
            """)
    List<KhachHangResponse> search(@Param("keyword") String keyword);
}
