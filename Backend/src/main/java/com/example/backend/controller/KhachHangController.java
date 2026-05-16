package com.example.backend.controller;

import com.example.backend.request.KhachHangRequest;
import com.example.backend.response.KhachHangResponse;
import com.example.backend.service.KhachHangService;
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
@RequestMapping("/api/shop-ban-quan-ao/khach-hang")
@CrossOrigin(origins = "*")
public class KhachHangController {
    @Autowired
    KhachHangService khachHangServcie;
    @GetMapping("/page")
    public List<KhachHangResponse> getKhachHang(@RequestParam(value = "pageNo",defaultValue = "0") Integer pageNo,
                                                @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize) {
        return khachHangServcie.phanTrangKhachHang(pageNo, pageSize).getContent();
    }
    @DeleteMapping("/delete/{id}")
    public void deleteKhachHang(@PathVariable Integer id) {
        khachHangServcie.detailKhachHang(id);
    }
    @GetMapping("/detail/{id}")
    public KhachHangResponse detailKhachHang(@PathVariable Integer id) {
        return khachHangServcie.detailKhachHang(id);
    }
    @PostMapping("/add")
    public void addKhachHang(@RequestPart("data") KhachHangRequest khachHangRequest, @RequestPart(value = "file", required = false) MultipartFile file) {
        khachHangServcie.addKhachHang(khachHangRequest, file);
    }
    @PutMapping("/update/{id}")
    public void updateKhachHang(@PathVariable Integer id, @RequestPart("data") KhachHangRequest khachHangRequest, @RequestPart(value = "file", required = false) MultipartFile file) {
        khachHangServcie.updateKhachHang(id, khachHangRequest, file);
    }
    @GetMapping("/search")
    public List<KhachHangResponse> timKiem(@RequestParam String keyword) {
        return khachHangServcie.searchFullKhachHang(keyword);
    }
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "uploads", "khachhang", filename);
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

    @GetMapping("/export-excel")
    public ResponseEntity<Resource> exportExcel(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Boolean gender,
                                                @RequestParam(required = false) Integer status) {
        // 1. Gọi service để lấy luồng dữ liệu Excel
        ByteArrayInputStream in = khachHangServcie.khachHangExcel(keyword, gender, status);

        // 2. Thiết lập tên file với thời gian hiện tại để tránh trùng lặp
        String filename = "DanhSachNhanVien_" + System.currentTimeMillis() + ".xlsx";
        InputStreamResource file = new InputStreamResource(in);

        // 3. Trả về ResponseEntity kèm các Header cần thiết cho việc tải file
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
}
