package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Product;
import com.bca.byc.entity.ProductCategory;
import com.bca.byc.model.CompilerFilterRequest;
import com.bca.byc.model.ProductDetailResponse;
import com.bca.byc.model.ProductIndexResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.ProductCategoryRepository;
import com.bca.byc.repository.ProductRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.util.FileUploadHelper.saveFile;

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
        ListOfFilterPagination filter = new ListOfFilterPagination(f.keyword());
        SavedKeywordAndPageable set = GlobalConverter.createPageable(f.pages(), f.limit(), f.sortBy(), f.direction(), f.keyword(), filter);

        Page<Product> pageResult = productRepository.listDataProduct(set.keyword(), set.pageable());
        List<ProductIndexResponse> dtos = pageResult.stream().map((c) -> {
            ProductIndexResponse dto = new ProductIndexResponse();
            dto.setName(c.getName());
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
        Product data = HandlerRepository.getEntityBySecureId(id, productRepository, "Business position not found");
        return new ProductDetailResponse(
                data.getSecureId(),
                data.getName(),
                data.getDescription(),
                GlobalConverter.getAvatarImage(data.getImage(), baseUrl),
                data.getCategory().getName(),
                data.getPrice(),
                data.getIsActive()
        );
    }

    @Override
    public void saveData(MultipartFile file, String name, String description, String categoryId, Integer price, Boolean isActive) throws IOException {
        Product data = new Product();
        saveDataParsing(data, file, name, description, categoryId, price, isActive);
    }

    @Override
    public void updateData(String id, MultipartFile file, String name, String description, String categoryId, Integer price, Boolean isActive) throws IOException {
        Product data = HandlerRepository.getEntityBySecureId(id, productRepository, "Business position not found");
        saveDataParsing(data, file, name, description, categoryId, price, isActive);
    }

    @Override
    public void deleteData(String id) {
        Product data = HandlerRepository.getEntityBySecureId(id, productRepository, "Business position not found");
        productRepository.delete(data);
    }

    // -- parsing

    private void saveDataParsing(Product data, MultipartFile file, String name, String description, String categoryId, Integer price, Boolean isActive) throws IOException {
        Long adminLoginId = ContextPrincipal.getId();

        data.setName(name); // set name
        data.setDescription(description); // set description
        ProductCategory category = HandlerRepository.getEntityBySecureId(categoryId, productCategoryRepository, "Business position not found");
        data.setCategory(category); // set category
        data.setPrice(price); // set price
        data.setIsActive(isActive); // set isActive

        if (file != null) {
            String imageUrl = saveFile(file, UPLOAD_DIR + "/admin/image");
            String imagePath = GlobalConverter.replaceImagePath(imageUrl);
            data.setImage(imagePath);
        }

        GlobalConverter.CmsAdminCreateAtBy(data, adminLoginId);

        productRepository.save(data);
    }
}
