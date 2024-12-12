package com.kembang.service;

import com.kembang.model.CompilerFilterRequest;
import com.kembang.model.ProductDetailResponse;
import com.kembang.model.ProductIndexResponse;
import com.kembang.response.ResultPageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ResultPageResponseDTO<ProductIndexResponse> listDataProduct(CompilerFilterRequest f);

    ProductDetailResponse findDataBySecureId(String id);

    void saveData(MultipartFile file, String name, String code, String description, String categoryId, Integer price, Boolean isActive) throws IOException;

    void updateData(String id, MultipartFile file, String name, String code, String description, String categoryId, Integer price, Boolean isActive) throws IOException;

    void deleteData(String id);
}
