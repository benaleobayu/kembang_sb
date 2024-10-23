package com.bca.byc.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompilerFilterRequest {
    private Integer pages;
    private Integer limit;
    private String sortBy;
    private String direction;
    private String keyword;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean status;

    public CompilerFilterRequest(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        this.pages = pages;
        this.limit = limit;
        this.sortBy = sortBy;
        this.direction = direction;
        this.keyword = keyword;
    }

    public CompilerFilterRequest(Integer pages, Integer limit, String sortBy, String direction, String keyword, LocalDate startDate, LocalDate endDate, Boolean status) {
        this.pages = pages;
        this.limit = limit;
        this.sortBy = sortBy;
        this.direction = direction;
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
}
