package com.bca.byc.service.impl;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.model.AdminCmsDetailResponse;
import com.bca.byc.model.AdminCreateRequest;
import com.bca.byc.model.AdminDetailResponse;
import com.bca.byc.model.AdminUpdateRequest;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Override
    public AdminDetailResponse findDataById(Long id) throws Exception {
        return null;
    }

    @Override
    public List<AdminDetailResponse> findAllData() {
        return null;
    }

    @Override
    public void saveData(AdminCreateRequest dto) throws Exception {

    }

    @Override
    public void updateData(Long id, AdminUpdateRequest dto) throws Exception {

    }

    @Override
    public void deleteData(Long id) throws Exception {

    }

    @Override
    public ResultPageResponseDTO<AdminDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        return null;
    }

    @Override
    public AdminCmsDetailResponse getAdminDetail(String email) {
        return null;
    }

    @Override
    public AppAdmin findByEmail(String email) {
        return null;
    }
}
