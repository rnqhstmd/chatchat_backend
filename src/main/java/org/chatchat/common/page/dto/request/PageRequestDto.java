package org.chatchat.common.page.dto.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PageRequestDto(int page) {
    private static final int MESSAGE_PAGE_SIZE = 20;

    public PageRequestDto(int page) {
        this.page = Math.max(page - 1, 0);
    }

    public Pageable toMessagePageable() {
        // Pageable 객체 생성
        return PageRequest.of(page, MESSAGE_PAGE_SIZE);
    }
}

