package com.example.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailImpl {
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Hàm gửi email thông tin tài khoản
     * @param toEmail: Email người nhận
     * @param tenTaiKhoan: Tên hiển thị của khách hàng
     * @param matKhauRaw: Mật khẩu chưa mã hóa để khách hàng biết
     */
    public void sendAccountEmail(String toEmail, String tenTaiKhoan, String matKhauRaw) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("nguyenvanan23096@gmail.com");
            message.setTo(toEmail);
            message.setSubject("THÔNG BÁO TẠO TÀI KHOẢN THÀNH CÔNG");

            String content = "Xin chào " + tenTaiKhoan + ",\n\n" +
                    "Chào mừng bạn đã gia nhập hệ thống của chúng tôi.\n" +
                    "Dưới đây là thông tin đăng nhập của bạn:\n" +
                    "--------------------------------------\n" +
                    "Tên tài khoản: " + tenTaiKhoan + "\n" +
                    "Email đăng nhập: " + toEmail + "\n" +
                    "Mật khẩu: " + matKhauRaw + "\n" +
                    "--------------------------------------\n" +
                    "Vui lòng đăng nhập và đổi mật khẩu ngay để bảo mật thông tin.\n" +
                    "Trân trọng!";

            message.setText(content);

            mailSender.send(message);
            System.out.println("=> Đã gửi mail thành công cho: " + toEmail);

        } catch (Exception e) {
            System.err.println("=> Lỗi khi gửi mail: " + e.getMessage());
        }
    }
}
