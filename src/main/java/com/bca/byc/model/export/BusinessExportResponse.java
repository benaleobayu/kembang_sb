package com.bca.byc.model.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessExportResponse {

    private Long id;
    private String name;
    private String address;
    private String lineOfBusiness;
    private Boolean isPrimary;
    private List<String> locations = new ArrayList<>();
    private List<String> subCategories = new ArrayList<>();

    public BusinessExportResponse(Long id, String name, String address, String lineOfBusiness, Boolean isPrimary) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.lineOfBusiness = lineOfBusiness;
        this.isPrimary = isPrimary;
    }
}
