package com.bca.byc.model.search;

import com.bca.byc.model.attribute.PostContentRequest;
import lombok.Data;

@Data
public class SearchResultPostResponse {

    private Long  id;

    private PostContentRequest content;

    private String avatar;

    private String name;

}
