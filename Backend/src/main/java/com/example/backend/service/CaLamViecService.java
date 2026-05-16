package com.example.backend.service;

import com.example.backend.model.CaLamViec;
import com.example.backend.request.CaLamViecRequest;
import com.example.backend.response.CaLamViecResponse;
import org.springframework.data.domain.Page;

public interface CaLamViecService {
       Page<CaLamViecResponse> phanTrangCaLamViec(Integer pageNo, Integer pageSize);

       void deleleteCaLamViec(Integer id);
       void addCaLamViec(CaLamViecRequest caLamViecRequest);
       void updateCaLamViec(Integer id,CaLamViecRequest caLamViecRequest);
       CaLamViecResponse detailCaLamViec(Integer id);
}
