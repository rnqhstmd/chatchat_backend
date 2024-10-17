package org.chatchat.common.page.dto.response;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        int currentPage,
        int totalPages
) {
    public static <T> PageResponseDto<T> of(List<T> content, int currentPage, int totalPages) {
        return new PageResponseDto<>(content, currentPage, totalPages);
    }
}
