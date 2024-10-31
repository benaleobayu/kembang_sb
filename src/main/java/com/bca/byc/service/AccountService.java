package com.bca.byc.service;

import com.bca.byc.model.AccountDetailResponse;
import com.bca.byc.model.AccountIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface AccountService {

    ResultPageResponseDTO<AccountIndexResponse> listDataAccountIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    AccountDetailResponse findDataById(String id) throws BadRequestException;

    void saveData(MultipartFile avatar, MultipartFile cover, String name, Boolean status, Set<String> channelIds, String email, String password) throws IOException;

    void updateData(String id, MultipartFile avatar, MultipartFile cover, String name, Set<String> channelIds, Boolean status) throws IOException;

    void deleteData(String id) throws BadRequestException;
}
