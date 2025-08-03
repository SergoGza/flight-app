package com.tokioschool.flightapp.base.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@Jacksonized
public class BookDTO {

    int id;
    String title;
    String genre;
    List<Integer> authorId;
}
