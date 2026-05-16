package com.example.backend.repository;

import com.example.backend.model.VaiTro;
import com.example.backend.response.VaiTroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, Integer> {
       @Query("""
       SELECT NEW com.example.backend.response.VaiTroResponse(vt.id,vt.maVaiTro,vt.tenVaiTro,vt.trangThai) 
       FROM VaiTro vt
""")
       Page<VaiTroResponse> phanTrang(Pageable pageable);
       @Query("""
       SELECT NEW com.example.backend.response.VaiTroResponse(vt.id,vt.maVaiTro,vt.tenVaiTro,vt.trangThai) 
       FROM VaiTro vt
""")
       List<VaiTroResponse> hienThi();

    Optional<VaiTro> findByTenVaiTro(String tenVaiTro);}
