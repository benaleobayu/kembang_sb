package com.bca.byc.response;


import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class Page<T> extends PageImpl<T> implements Serializable {

    public Page(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public Page(List<T> content) {
        super(content);
    }
}
