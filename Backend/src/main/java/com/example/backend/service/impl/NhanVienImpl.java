package com.example.backend.service.impl;

import com.example.backend.model.NhanVien;
import com.example.backend.model.TaiKhoan;
import com.example.backend.model.VaiTro;
import com.example.backend.repository.NhanVienRepository;
import com.example.backend.repository.TaiKhoanRepository;
import com.example.backend.repository.VaiTroRepository;
import com.example.backend.request.NhanVienRequest;
import com.example.backend.response.NhanVienResponse;
import com.example.backend.service.NhanVienService;
import jakarta.persistence.criteria.Predicate;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class NhanVienImpl implements NhanVienService {
    @Autowired
    NhanVienRepository nhanVienRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    @Autowired
    VaiTroRepository vaiTroRepository;

    @Override
    public Page<NhanVienResponse> phanTrangNhanVien(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return nhanVienRepository.phanTrang(pageable);
    }

    @Override
    public void deleteNhanVien(Integer id) {
        NhanVien nhanVien = nhanVienRepository.findById(id).get();
        nhanVien.setTrangThai(0);
        nhanVienRepository.save(nhanVien);
    }

    @Override
    @Transactional
    public void addNhanVien(NhanVienRequest nhanVienRequest, MultipartFile file) {
        // 1. Tạo và lưu Tài Khoản trước
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setEmail(nhanVienRequest.getEmail());
        taiKhoan.setMatKhau(nhanVienRequest.getMatKhau());
        taiKhoan.setSoDienThoai(nhanVienRequest.getSoDienThoai());
        taiKhoan.setTrangThai(1);
        //vai trò
        VaiTro vaiTro = vaiTroRepository.findById(nhanVienRequest.getIdVaiTro())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
        taiKhoan.setIdVaiTro(vaiTro);
        TaiKhoan savedTk = taiKhoanRepository.save(taiKhoan); // Lưu để lấy ID tự tăng
        // 2. Tạo đối tượng Nhân Viên
        NhanVien nhanVien = new NhanVien();
        BeanUtils.copyProperties(nhanVienRequest, nhanVien);

        // 3. LIÊN KẾT: Gán ID tài khoản vào nhân viên
        nhanVien.setIdTaiKhoan(savedTk);
        if (nhanVien.getMaNhanVien() == null || nhanVien.getMaNhanVien().isEmpty()) {
            // Thay vì dùng millis dài dằng dặc, ta dùng format ngày: NV + ddMMyy + HHmmss
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmmss"));
            nhanVien.setMaNhanVien("NV" + timeStamp);
        }
        // 5. Xử lý ảnh
        if (file != null && !file.isEmpty()) {
            String fileName = saveFive(file);
            nhanVien.setAnh(fileName);
        }

        nhanVien.setNgayTao(Instant.now());
        nhanVien.setTrangThai(1);
        nhanVienRepository.save(nhanVien);
    }

    private String saveFive(MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/nhanvien";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destiantion = new File(dir, fileName);
            file.transferTo(destiantion);
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi upload file: " + e.getMessage());
        }
    }

    @Override
    @Transactional // Quan trọng: Đảm bảo nếu lưu lỗi thì sẽ rollback dữ liệu
    public void updateNhanVien(Integer id, NhanVienRequest nhanVienRequest, MultipartFile file) {
        // 1. Tìm nhân viên, nếu không thấy thì báo lỗi rõ ràng
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + id));

        // 2. Lấy tài khoản liên kết từ nhân viên
        TaiKhoan taiKhoan = nhanVien.getIdTaiKhoan();
        if (taiKhoan == null) {
            throw new RuntimeException("Nhân viên này chưa có tài khoản!");
        }

        // 3. Cập nhật thông tin Tài Khoản trước
        taiKhoan.setEmail(nhanVienRequest.getEmail());
        taiKhoan.setSoDienThoai(nhanVienRequest.getSoDienThoai());

        // Chỉ cập nhật mật khẩu nếu người dùng có nhập mới
        if (nhanVienRequest.getMatKhau() != null && !nhanVienRequest.getMatKhau().isBlank()) {
            taiKhoan.setMatKhau(nhanVienRequest.getMatKhau());
        }

        // Cập nhật vai trò
        if (nhanVienRequest.getIdVaiTro() != null) {
            VaiTro vt = vaiTroRepository.findById(nhanVienRequest.getIdVaiTro())
                    .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
            taiKhoan.setIdVaiTro(vt);
        }
        taiKhoanRepository.save(taiKhoan);

        // 4. Cập nhật thông tin Nhân Viên
        // Tránh dùng copyProperties trực tiếp lên ID để an toàn
        nhanVien.setTenNhanVien(nhanVienRequest.getTenNhanVien());
        nhanVien.setGioiTinh(nhanVienRequest.getGioiTinh());
        nhanVien.setNgaySinh(nhanVienRequest.getNgaySinh());
        nhanVien.setCanCuocCongDan(nhanVienRequest.getCanCuocCongDan());
        nhanVien.setDiaChi(nhanVienRequest.getDiaChi());
        nhanVien.setTrangThai(nhanVienRequest.getTrangThai());

        // 5. Xử lý file ảnh
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = saveFive(file); // Đảm bảo hàm này trả về tên file hợp lệ
                nhanVien.setAnh(fileName);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi khi lưu file ảnh");
            }
        }

        // Lưu nhân viên (taiKhoan đã được cập nhật ở bước 3)
        nhanVienRepository.save(nhanVien);
    }
    @Override
    public List<NhanVienResponse> searchFullNhanVien(String keyword) {
        return nhanVienRepository.search(keyword);
    }

    @Override
    public NhanVienResponse detailNhanVien(Integer id) {
        return nhanVienRepository.detailNhanVien(id);
    }

    @Override
    public ByteArrayInputStream nhanVienExcel(String keyword, Boolean gender, Integer status) {
        List<NhanVien> nhanVienList = nhanVienRepository.findAll(createSpecification(keyword, gender, status));
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Đanh Sách Nhân Viên");
            String[] headers = {"Mã nhân viên", "Tên nhân viên", "CCCD", "SĐT", "Email", "Vai trò", "Địa chỉ", "Ngày sinh", "Giới tính", "Trạng thái"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            int rowIdx = 1;
            for (NhanVien nhanVien : nhanVienList) {
                Row row = sheet.createRow(rowIdx++);
                Object[] rowData = {
                        nhanVien.getMaNhanVien(),
                        nhanVien.getTenNhanVien(),
                        nhanVien.getCanCuocCongDan(),
                        nhanVien.getIdTaiKhoan() != null ? nhanVien.getIdTaiKhoan().getSoDienThoai() : "",
                        nhanVien.getIdTaiKhoan() != null ? nhanVien.getIdTaiKhoan().getEmail() : "",
                        nhanVien.getIdTaiKhoan() != null
                                && nhanVien.getIdTaiKhoan().getIdVaiTro() != null
                                ? nhanVien.getIdTaiKhoan().getIdVaiTro().getTenVaiTro()
                                : "",
                        nhanVien.getDiaChi(),
                        nhanVien.getNgaySinh(),
                        Boolean.TRUE.equals(nhanVien.getGioiTinh()) ? "Nam" : "Nữ",
                        Integer.valueOf(1).equals(nhanVien.getTrangThai()) ? "Đang làm" : "Nghỉ việc"
                };
                for (int i = 0; i < rowData.length; i++) {
                    row.createCell(i).setCellValue(rowData[i] != null ? String.valueOf(rowData[i]) : "");
                }
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi xuất file excel: " + e.getMessage());
        }
    }

    private Specification<NhanVien> createSpecification(String keyword, Boolean gender, Integer status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.trim() + "%";
                String[] localFields = {
                        "maNhanVien", "tenNhanVien", "canCuocCongDan",
                        "soDienThoai", "diaChi", "ngaySinh", "gioiTinh", "trangThai"
                };
                Stream<Predicate> localPredicates = Stream.of(localFields)
                        .map(field -> cb.like(root.get(field).as(String.class), pattern));
                Predicate taiKhoanPredicate =cb.or(
                        cb.like(root.join("idTaiKhoan").get("email").as(String.class), pattern),
                        cb.like(root.join("idTaiKhoan").get("soDienThoai").as(String.class), pattern)
                );
                Predicate vaiTroPredicate = cb.like(root.join("idTaiKhoan").join("vaiTro").get("tenVaiTro").as(String.class), pattern);
                Predicate finalKeywordPredicate = cb.or(
                        Stream.concat(localPredicates, Stream.of(taiKhoanPredicate, vaiTroPredicate))
                                .toArray(Predicate[]::new)
                );
                predicates.add(finalKeywordPredicate);
            }
            if (gender != null) {
                predicates.add(cb.equal(root.get("gioiTinh"), gender));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("trangThai"), status));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }


}
