package com.bca.byc.converter.dictionary;

import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.util.PaginationUtil;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageCreateReturn {

    public static ResultPageResponseDTO create(Page<?> pageResult, List<?> dtos) {
        int currentPage = pageResult.getNumber() + 1;
        int totalPages = pageResult.getTotalPages();

        return PaginationUtil.createResultPageDTO(
                pageResult.getTotalElements(), // total items
                dtos,
                currentPage, // current page
                currentPage > 1 ? currentPage - 1 : 1, // prev page
                currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1, // next page
                1, // first page
                totalPages - 1, // last page
                pageResult.getSize() // per page
        );
    }
}
