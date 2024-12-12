package com.kembang.converter.dictionary;

import com.kembang.response.ResultPageResponseDTO;
import com.kembang.util.PaginationUtil;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageCreateReturnApps {

    public static ResultPageResponseDTO create(Page<?> pageResult, List<?> dtos) {
        int currentPage = pageResult.getNumber();
        int totalPages = pageResult.getTotalPages();

        return PaginationUtil.createResultPageDTO(
                pageResult.getTotalElements(), // total items
                dtos,
                currentPage, // current page
                currentPage >= 1 ? currentPage - 1 : null, // prev page
                currentPage < totalPages - 1 ? currentPage + 1 : null, // next page
                0, // first page
                totalPages - 1 == 0 ? null : totalPages - 1, // last page
                pageResult.getSize() // per page
        );
    }
}
