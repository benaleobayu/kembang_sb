package com.kembang.service;

import com.kembang.model.CompilerFilterRequest;
import com.kembang.model.SimpleCmsResponse;
import com.kembang.model.SimpleCreateUpdateRequest;
import com.kembang.response.ResultPageResponseDTO;

public interface ProductCategoryService {

    ResultPageResponseDTO<SimpleCmsResponse> listDataProductCategory(CompilerFilterRequest f);

    SimpleCmsResponse findDataBySecureId(String id);

    void saveData(SimpleCreateUpdateRequest dto);

    void updateData(String id, SimpleCreateUpdateRequest dto);

    void deleteData(String id);
}
