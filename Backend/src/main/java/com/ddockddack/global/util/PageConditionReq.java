package com.ddockddack.global.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageConditionReq {

    private String order;
    private String period;
    private String search;
    private String keyword;
    private Integer page = 1;

    public PageCondition toEntity() {
        return PageCondition.builder()
                .order(this.order)
                .period(this.period)
                .search(this.search)
                .keyword(this.keyword)
                .page(this.page-1)
                .build();
    }


}
