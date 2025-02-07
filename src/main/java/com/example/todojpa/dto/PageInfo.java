package com.example.todojpa.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageInfo {
    private int page;
    private int size;
    private int total;
    public PageInfo(Page page) {
        this.page = page.getNumber();
        this.size = page.getSize();
        this.total = (int) page.getTotalPages();
    }
}
