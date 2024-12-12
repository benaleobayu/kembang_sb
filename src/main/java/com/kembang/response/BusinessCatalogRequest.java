package com.kembang.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCatalogRequest {

    private String businessSecureId;
    private String title;
    private String image;
    private String description;

}
