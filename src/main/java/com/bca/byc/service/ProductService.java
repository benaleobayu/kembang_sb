package com.bca.byc.service;

import com.bca.byc.model.CompilerFilterRequest;
import com.bca.byc.model.ProductDetailResponse;
import com.bca.byc.model.ProductIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ResultPageResponseDTO<ProductIndexResponse> listDataProduct(CompilerFilterRequest f);

    ProductDetailResponse findDataBySecureId(String id);

    void saveData(MultipartFile file, String name, String description, String categoryId, Integer price, Boolean isActive) throws IOException;

    void updateData(String id, MultipartFile file, String name, String description, String categoryId, Integer price, Boolean isActive) throws IOException;

    void deleteData(String id);
}
