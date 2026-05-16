package com.example.backend.service.impl;

import com.example.backend.model.KhachHang;
import com.example.backend.model.TaiKhoan;
import com.example.backend.model.VaiTro;
import com.example.backend.repository.DiaChiKhachHangRepository;
import com.example.backend.repository.KhachHangRepository;
import com.example.backend.repository.TaiKhoanRepository;
import com.example.backend.repository.VaiTroRepository;
import com.example.backend.request.KhachHangRequest;
import com.example.backend.response.KhachHangResponse;
import com.example.backend.service.KhachHangService;
import jakarta.persistence.criteria.Predicate;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class KhachHangImpl implements KhachHangService {
    @Autowired
    KhachHangRepository khachHangRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    @Autowired
    DiaChiKhachHangRepository diaChiKhachRepository;
    @Autowired
    VaiTroRepository vaiTroRepository;
    @Autowired
    private EmailImpl emailImpl;

    @Override
    public Page<KhachHangResponse> phanTrangKhachHang(Integer pageNo, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        return khachHangRepository.phanTrang(pageRequest);
    }

    @Override
    @Transactional
    public void addKhachHang(KhachHangRequest khachHangRequest, MultipartFile file) {
        // Kiểm tra email trùng trước để tránh lỗi dữ liệu
        if (taiKhoanRepository.findByEmail(khachHangRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại trên hệ thống!");
        }

        // 1. Tạo mật khẩu ngẫu nhiên
        String rawPassword = RandomPassword.generate(8);

        // 2. Tạo và cấu hình TaiKhoan mới
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setEmail(khachHangRequest.getEmail());
        taiKhoan.setTenTaiKhoan(khachHangRequest.getTenTaiKhoan());
        taiKhoan.setSoDienThoai(khachHangRequest.getSoDienThoai());
        taiKhoan.setMatKhau(rawPassword);
        taiKhoan.setTrangThai(1);

        VaiTro vaiTroMacDinh = new VaiTro();
        vaiTroMacDinh.setId(3);
        taiKhoan.setIdVaiTro(vaiTroMacDinh);

        // Lưu tài khoản để lấy ID tự tăng
        TaiKhoan savedTaiKhoan = taiKhoanRepository.save(taiKhoan);

        // 3. Tạo và lưu thông tin KhachHang
        KhachHang khachHang = new KhachHang();

        // Xử lý upload ảnh cá nhân
        if (file != null && !file.isEmpty()) {
            String fileName = saveFive(file);
            khachHang.setAnh(fileName);
        }

        // Thiết lập mối quan hệ với tài khoản
        khachHang.setIdTaiKhoan(savedTaiKhoan);

        // Tự sinh mã khách hàng dựa trên thời gian thực
        khachHang.setMaKhachHang("KH" + System.currentTimeMillis());
        khachHang.setHoTen(khachHangRequest.getHoTen());
        khachHang.setNgaySinh(khachHangRequest.getNgaySinh());

        // =========================================================================
        // SỬA TẠI ĐÂY: Gán giá trị số điện thoại cho Khách hàng để tránh lỗi NOT NULL trong DB
        // =========================================================================
        khachHang.setSoDienThoai(khachHangRequest.getSoDienThoai());
        // =========================================================================

        // Xử lý giới tính dạng boolean an toàn
        khachHang.setGioiTinh("Nam".equalsIgnoreCase(khachHangRequest.getGioiTinh()));
        khachHang.setTrangThai(1);

        // Gộp các phần địa chỉ rời thành một chuỗi hoàn chỉnh để hiện lên bảng chính
        String diaChiFull = Stream.of(
                        khachHangRequest.getDiaChiCuThe(),
                        khachHangRequest.getPhuong(),
                        khachHangRequest.getQuan(),
                        khachHangRequest.getThanhPho()
                )
                .filter(str -> str != null && !str.trim().isEmpty())
                .collect(Collectors.joining(", "));
        khachHang.setDiaChi(diaChiFull);

        khachHangRepository.save(khachHang);

        // 4. Gửi mail thông báo tài khoản về cho khách hàng
        emailImpl.sendAccountEmail(
                savedTaiKhoan.getEmail(),
                savedTaiKhoan.getTenTaiKhoan(),
                rawPassword
        );

        System.out.println("Thêm khách hàng thành công. Mật khẩu gửi đi: " + rawPassword);
    }
    private String saveFive(MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/khachhang";
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
    @Transactional
    public void updateKhachHang(Integer id, KhachHangRequest khachHangRequest, MultipartFile file) {

        // 1. Tìm khách hàng cũ theo ID
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));

        // 2. Cập nhật thông tin tài khoản đính kèm (giữ nguyên vai trò cũ)
        TaiKhoan taiKhoan = khachHang.getIdTaiKhoan();
        if (taiKhoan != null) {
            // Kiểm tra xem email mới thay đổi có bị trùng với người khác không
            Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findByEmail(khachHangRequest.getEmail());
            if (optionalTaiKhoan.isPresent() && !optionalTaiKhoan.get().getId().equals(taiKhoan.getId())) {
                throw new RuntimeException("Email này đã được sử dụng bởi một tài khoản khác!");
            }

            taiKhoan.setEmail(khachHangRequest.getEmail());
            taiKhoan.setTenTaiKhoan(khachHangRequest.getTenTaiKhoan());
            taiKhoan.setSoDienThoai(khachHangRequest.getSoDienThoai());

            // Đồng bộ dữ liệu tài khoản xuống DB
            taiKhoanRepository.save(taiKhoan);
        }

        // 3. Cập nhật ảnh mới (Nếu người dùng chọn file mới)
        if (file != null && !file.isEmpty()) {
            String fileName = saveFive(file);
            khachHang.setAnh(fileName);
        }

        // 4. Cập nhật các thông tin cơ bản của khách hàng
        khachHang.setHoTen(khachHangRequest.getHoTen());
        khachHang.setNgaySinh(khachHangRequest.getNgaySinh());
        khachHang.setTrangThai(khachHangRequest.getTrangThai());
        khachHang.setGioiTinh("Nam".equalsIgnoreCase(khachHangRequest.getGioiTinh()));

        // =========================================================================
        // SỬA TẠI ĐÂY: Đồng bộ luôn số điện thoại mới sang bảng khach_hang khi update
        // =========================================================================
        khachHang.setSoDienThoai(khachHangRequest.getSoDienThoai());
        // =========================================================================

        // Gộp chuỗi địa chỉ ngăn nắp, loại bỏ trường bị trống
        String diaChiFull = Stream.of(
                        khachHangRequest.getDiaChiCuThe(),
                        khachHangRequest.getPhuong(),
                        khachHangRequest.getQuan(),
                        khachHangRequest.getThanhPho()
                )
                .filter(str -> str != null && !str.trim().isEmpty())
                .collect(Collectors.joining(", "));

        khachHang.setDiaChi(diaChiFull);

        // 5. Lưu toàn bộ thay đổi của Khách Hàng xuống DB
        khachHangRepository.save(khachHang);
    }
    //got

    @Override
    public List<KhachHangResponse> searchFullKhachHang(String keyword) {
        return khachHangRepository.search(keyword);
    }

    @Override
    public KhachHangResponse detailKhachHang(Integer id) {
        return khachHangRepository.detail(id);
    }

    @Override
    public ByteArrayInputStream khachHangExcel(String keyword, Boolean gender, Integer status) {
        List<KhachHang> khachHangList = khachHangRepository.findAll(createSpecification(keyword, gender, status));

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Danh Sách Khách Hàng");

            // 1. Tạo Header
            String[] headers = {"Mã khách hàng", "Họ và tên", "Email", "Tài khoản", "SĐT", "Địa chỉ", "Giới tính", "Trạng thái"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 2. Đổ dữ liệu
            int rowIdx = 1;
            for (KhachHang khachHang : khachHangList) {
                Row row = sheet.createRow(rowIdx++);

                // Lấy thông tin từ bảng TaiKhoan liên kết
                String email = "";
                String tenTaiKhoan = "";
                if (khachHang.getIdTaiKhoan() != null) {
                    email = khachHang.getIdTaiKhoan().getEmail();
                    tenTaiKhoan = khachHang.getIdTaiKhoan().getTenTaiKhoan();
                }

                Object[] rowData = {
                        khachHang.getMaKhachHang(),
                        khachHang.getHoTen(),
                        email,           // Cột Email
                        tenTaiKhoan,     // Cột Tài khoản
                        khachHang.getSoDienThoai(),
                        khachHang.getDiaChi(),
                        Boolean.TRUE.equals(khachHang.getGioiTinh()) ? "Nam" : "Nữ",
                        Integer.valueOf(1).equals(khachHang.getTrangThai()) ? "Hoạt động" : "Không hoạt động"
                };

                for (int j = 0; j < rowData.length; j++) {
                    row.createCell(j).setCellValue(rowData[j] != null ? String.valueOf(rowData[j]) : "");
                }
            }

            // 3. Tự động căn chỉnh độ rộng cột (Cần căn chỉnh theo số lượng headers)
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi xuất file excel: " + e.getMessage());
        }
    }

    private Specification<KhachHang> createSpecification(String keyword, Boolean gender, Integer status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                String[] localFields = {"maKhachHang", "tenKhachHang", "email","tenTaiKhoan", "soDienThoai", "diaChi"};

                // SỬA Ở ĐÂY: Thêm .toArray(...) để chuyển từ Stream sang Array
                Predicate[] localPredicates = Stream.of(localFields)
                        .map(field -> cb.like(cb.lower(root.get(field).as(String.class)), pattern))
                        .toArray(Predicate[]::new);

                // Bây giờ truyền mảng vào cb.or sẽ hết lỗi
                predicates.add(cb.or(localPredicates));
            }
            if (gender != null) predicates.add(cb.equal(root.get("gioiTinh"), gender));
            if (status != null) predicates.add(cb.equal(root.get("trangThai"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
