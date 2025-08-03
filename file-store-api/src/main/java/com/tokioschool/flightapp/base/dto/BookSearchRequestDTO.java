package com.tokioschool.flightapp.base.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BookSearchRequestDTO {
    String genre;
    int page;
    int pageSize;
}
