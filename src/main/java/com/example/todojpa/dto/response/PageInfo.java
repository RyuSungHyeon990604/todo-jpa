package com.example.todojpa.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageInfo {
    private final int page;
    private final int size;
    private final int total;
    public PageInfo(Page page) {
        this.page = page.getNumber();
        this.size = page.getSize();
        this.total = (int) page.getTotalPages();
    }
}
