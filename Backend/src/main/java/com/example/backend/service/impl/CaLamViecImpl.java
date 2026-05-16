package com.example.backend.service.impl;

import com.example.backend.model.CaLamViec;
import com.example.backend.repository.CaLamViecRepository;
import com.example.backend.request.CaLamViecRequest;
import com.example.backend.response.CaLamViecResponse;
import com.example.backend.service.CaLamViecService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CaLamViecImpl implements CaLamViecService {
    @Autowired
    CaLamViecRepository caLamViecRepository;

    @Override
    public Page<CaLamViecResponse> phanTrangCaLamViec(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo , pageSize);
        return caLamViecRepository.phanTrang(pageable);
    }

    @Override
    public void deleleteCaLamViec(Integer id) {
        CaLamViec caLamViec = caLamViecRepository.findById(id).get();
        caLamViec.setTrangThai(0);
        caLamViecRepository.save(caLamViec);

    }

    @Override
    public void addCaLamViec(CaLamViecRequest caLamViecRequest) {
         CaLamViec caLamViec = new CaLamViec();
         BeanUtils.copyProperties(caLamViecRequest, caLamViec);
         caLamViecRepository.save(caLamViec);
    }

    @Override
    public void updateCaLamViec(Integer id, CaLamViecRequest caLamViecRequest) {
        CaLamViec caLamViec = caLamViecRepository.findById(id).get();
        BeanUtils.copyProperties(caLamViecRequest, caLamViec);
        caLamViecRepository.save(caLamViec);
    }

    @Override
    public CaLamViecResponse detailCaLamViec(Integer id) {
        return caLamViecRepository.detail(id);
    }
}
