package com.bca.byc.model.search;

import com.bca.byc.entity.Business;
import lombok.Data;

import javax.naming.Name;
import java.util.List;

@Data
public class SearchResultAccountResponse {

    private Long id;

    private String name;

    private String avatar;

    private List<Business> business; // get business name and business categories

    private Boolean isFollowed = false;

}
