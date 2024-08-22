package com.bca.byc.util;

import com.bca.byc.response.ResultPageResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PaginationUtil {

    public static <T> ResultPageResponse<T> createResultPageDTO(List<T> dtos, Long totalElements, Integer pages){
        ResultPageResponse<T> result = new ResultPageResponse<T>();
        result.setPages(pages);
        result.setElements(totalElements);
        result.setResult(dtos);
        return result;
    }

    public static Sort.Direction getSortBy(String sortBy){
        if(sortBy.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        }else {
            return Sort.Direction.DESC;
        }
    }

}