package com.kembang.model;

public record CompilerFilterRequest(
        int pages,
        int limit,
        String sortBy,
        String direction,
        String keyword
) {
}
