package com.tokioschool.flightapp.domain;

import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString // Sólo para la prueba de concepto
public class Main {

  private String name;
  private List<Ingredient> ingredients;
}
