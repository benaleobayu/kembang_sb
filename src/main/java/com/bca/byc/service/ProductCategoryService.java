package com.bca.byc.service;

import com.bca.byc.model.CompilerFilterRequest;
import com.bca.byc.model.SimpleCmsResponse;
import com.bca.byc.model.SimpleCreateUpdateRequest;
import com.bca.byc.response.ResultPageResponseDTO;

public interface ProductCategoryService {

    ResultPageResponseDTO<SimpleCmsResponse> listDataProductCategory(CompilerFilterRequest f);

    SimpleCmsResponse findDataBySecureId(String id);

    void saveData(SimpleCreateUpdateRequest dto);

    void updateData(String id, SimpleCreateUpdateRequest dto);

    void deleteData(String id);
}
