package com.sumin.springdatajpa.common;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class PagingButtonInfo {
    private int currentPage;
    private int startPage;
    private int endPage;
}
