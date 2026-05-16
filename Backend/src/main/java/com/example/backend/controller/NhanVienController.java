package com.example.backend.controller;


import com.example.backend.request.NhanVienRequest;
import com.example.backend.response.NhanVienResponse;
import com.example.backend.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/shop-ban-quan-ao/nhan-vien")
@CrossOrigin(origins = "*")
public class NhanVienController {
    @Autowired
    NhanVienService nhanVienService;

    @GetMapping("/page")
    public List<NhanVienResponse> phanTrang(@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return nhanVienService.phanTrangNhanVien(pageNo, pageSize).getContent();
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        nhanVienService.deleteNhanVien(id);
    }

    @GetMapping("/search")
    public List<NhanVienResponse> search(@RequestParam String keyword) {
        return nhanVienService.searchFullNhanVien(keyword);
    }

    @PostMapping("/add")
    public void addNhanVien(@RequestPart("data") NhanVienRequest nhanVienRequest,
                            @RequestPart(value = "file", required = false) MultipartFile file) {
        nhanVienService.addNhanVien(nhanVienRequest, file);
    }

    @PutMapping("/update/{id}")
    public void updateNhanVien(@PathVariable Integer id, @RequestPart("data") NhanVienRequest nhanVienRequest,
                               @RequestPart(value = "file", required = false) MultipartFile file) {
        nhanVienService.updateNhanVien(id, nhanVienRequest, file);
    }

    @GetMapping("/detail/{id}")
    public NhanVienResponse detail(@PathVariable Integer id) {
        return nhanVienService.detailNhanVien(id);
    }

    @GetMapping("/export-excel")
    public ResponseEntity<Resource> exportExcel(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Boolean gender,
                                                @RequestParam(required = false) Integer status) {
        ByteArrayInputStream in = nhanVienService.nhanVienExcel(keyword, gender, status);
        String filename = "DanhSachNhanVien_" + System.currentTimeMillis() + ".xlsx";
        InputStreamResource file = new InputStreamResource(in);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "uploads", "nhanvien", filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
