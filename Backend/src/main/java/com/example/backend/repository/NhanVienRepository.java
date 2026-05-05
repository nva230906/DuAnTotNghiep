package com.example.backend.repository;

import com.example.backend.model.NhanVien;
import com.example.backend.response.NhanVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer>, JpaSpecificationExecutor<NhanVien> {
    @Query("""
                  SELECT NEW com.example.backend.response.NhanVienResponse(nv.id,nv.idTaiKhoan.id,nv.idTaiKhoan.email,nv.idTaiKhoan.soDienThoai,nv.idTaiKhoan.idVaiTro.id,nv.idTaiKhoan.idVaiTro.tenVaiTro,nv.maNhanVien,nv.tenNhanVien,nv.diaChi,nv.ngaySinh,nv.gioiTinh,nv.canCuocCongDan,nv.anh,nv.ngayTao,nv.ngayCapNhat,nv.nguoiTao,nv.nguoiCapNhat,nv.trangThai)
                  FROM NhanVien nv JOIN TaiKhoan tk ON nv.idTaiKhoan.id=tk.id
            """)
    Page<NhanVienResponse> phanTrang(Pageable pageable);
    @Query("""
     SELECT NEW com.example.backend.response.NhanVienResponse(nv.id,nv.idTaiKhoan.id,nv.idTaiKhoan.email,nv.idTaiKhoan.soDienThoai,nv.idTaiKhoan.idVaiTro.id,nv.idTaiKhoan.idVaiTro.tenVaiTro,nv.maNhanVien,nv.tenNhanVien,nv.diaChi,nv.ngaySinh,nv.gioiTinh,nv.canCuocCongDan,nv.anh,nv.ngayTao,nv.ngayCapNhat,nv.nguoiTao,nv.nguoiCapNhat,nv.trangThai)
                  FROM NhanVien nv JOIN TaiKhoan tk ON nv.idTaiKhoan.id=tk.id
    WHERE
        LOWER(tk.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(nv.maNhanVien) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(nv.tenNhanVien) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(nv.diaChi) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(nv.canCuocCongDan) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<NhanVienResponse> search(@Param("keyword") String keyword);

    @Query("""
               SELECT NEW com.example.backend.response.NhanVienResponse(nv.id,nv.idTaiKhoan.id,nv.idTaiKhoan.email,nv.idTaiKhoan.soDienThoai,nv.idTaiKhoan.idVaiTro.id,nv.idTaiKhoan.idVaiTro.tenVaiTro,nv.maNhanVien,nv.tenNhanVien,nv.diaChi,nv.ngaySinh,nv.gioiTinh,nv.canCuocCongDan,nv.anh,nv.ngayTao,nv.ngayCapNhat,nv.nguoiTao,nv.nguoiCapNhat,nv.trangThai)
               FROM NhanVien nv JOIN TaiKhoan tk ON nv.idTaiKhoan.id=tk.id
               Where nv.id=?1
            """)
    NhanVienResponse detailNhanVien(Integer id);

}
