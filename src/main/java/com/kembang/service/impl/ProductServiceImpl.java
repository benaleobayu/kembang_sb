package com.kembang.service.impl;

import com.kembang.converter.dictionary.PageCreateReturnApps;
import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.Product;
import com.kembang.entity.ProductCategory;
import com.kembang.model.CompilerFilterRequest;
import com.kembang.model.ProductDetailResponse;
import com.kembang.model.ProductIndexResponse;
import com.kembang.model.search.ListOfFilterPagination;
import com.kembang.model.search.SavedKeywordAndPageable;
import com.kembang.repository.ProductCategoryRepository;
import com.kembang.repository.ProductRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.security.util.ContextPrincipal;
import com.kembang.service.ProductService;
import com.kembang.util.FileUploadHelper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.kembang.util.FileUploadHelper.saveFile;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final AppAdminRepository adminRepository;

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Value("${app.base.url}")
    private String baseUrl;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Override
    public ResultPageResponseDTO<ProductIndexResponse> listDataProduct(CompilerFilterRequest f) {
        // pageable
        Page<Product> firstResult = productRepository.listDataProduct(null , null);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(f.pages(), f.limit(), f.sortBy(), f.direction(), f.keyword(), firstResult);

        // get data and stream
        Page<Product> pageResult = productRepository.listDataProduct(set.keyword(), set.pageable());
        List<ProductIndexResponse> dtos = pageResult.stream().map((c) -> {
            ProductIndexResponse dto = new ProductIndexResponse();
            dto.setName(c.getName() + " - [" + c.getCode() + "]");
            dto.setCode(c.getCode());
            dto.setDescription(c.getDescription());
            dto.setPrice(c.getPrice());
            dto.setImage(GlobalConverter.getAvatarImage(c.getImage(), baseUrl));
            dto.setCategory(c.getCategory().getName());
            dto.setIsActive(c.getIsActive());
            GlobalConverter.CmsIDTimeStampResponseAndId(dto, c, adminRepository);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public ProductDetailResponse findDataBySecureId(String id) {
        Product data = HandlerRepository.getEntityBySecureId(id, productRepository, "Product not found");
        return new ProductDetailResponse(
                data.getSecureId(),
                data.getName(),
                data.getCode(),
                data.getDescription(),
                GlobalConverter.getAvatarImage(data.getImage(), baseUrl),
                data.getCategory().getSecureId(),
                data.getPrice(),
                data.getIsActive()
        );
    }

    @Override
    public void saveData(MultipartFile file, String name, String code, String description, String categoryId, Integer price, Boolean isActive) throws IOException {
        Product data = new Product();
        saveDataParsing(data, file, name, code, description, categoryId, price, isActive, "create");
    }

    @Override
    public void updateData(String id, MultipartFile file, String name, String code, String description, String categoryId, Integer price, Boolean isActive) throws IOException {
        Product data = HandlerRepository.getEntityBySecureId(id, productRepository, "Product not found");
        saveDataParsing(data, file, name, code, description, categoryId, price, isActive, "update");
    }

    @Override
    public void deleteData(String id) {
        Product data = HandlerRepository.getEntityBySecureId(id, productRepository, "Product not found");
        productRepository.delete(data);
    }

    // -- parsing

    private void saveDataParsing(Product data, MultipartFile file, String name, String code,String description, String categoryId, Integer price, Boolean isActive, String type) throws IOException {
        Long adminLoginId = ContextPrincipal.getId();

        data.setName(name); // set name
        data.setCode(code); // set code
        data.setDescription(description); // set description
        ProductCategory category = HandlerRepository.getEntityBySecureId(categoryId, productCategoryRepository, "Product not found");
        data.setCategory(category); // set category
        data.setPrice(price); // set price
        data.setIsActive(isActive); // set isActive

        if (file != null) {
            handleImageFile(data, file, type);
        }

        GlobalConverter.CmsAdminCreateAtBy(data, adminLoginId);

        productRepository.save(data);
    }

    private void handleImageFile(Product data, MultipartFile file, String type) throws IOException {
        FileUploadHelper.validateFileTypeImage(file);
        String newCover = saveFile(file, UPLOAD_DIR + "/product");
        String formattedPath = GlobalConverter.replaceImagePath(newCover);

        if ("update".equals(type) && data.getImage() != null && !data.getImage().equals(formattedPath)) {
            FileUploadHelper.deleteFile(data.getImage(), UPLOAD_DIR);
        }
        data.setImage(formattedPath);
    }
}
