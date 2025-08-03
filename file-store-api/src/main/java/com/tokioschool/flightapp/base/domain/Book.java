package com.tokioschool.flightapp.base.domain;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Book {

  private Integer id;
  private String title;
  private String genre;
  private List<Author> authors;
}
