package com.tokioschool.flightapp.projection;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerStyleCountAggregate {

  private String style;
  private Long count;
}
