package com.bca.byc.model.search;

import org.springframework.data.domain.Pageable;

public record SavedKeywordAndPageable(
    String keyword,
    Pageable pageable
) {
}
