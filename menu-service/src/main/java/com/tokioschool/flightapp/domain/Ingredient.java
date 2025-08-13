package com.tokioschool.flightapp.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString // Solo para la prueba de concepto
@Document(collection = "ingredients")
public class Ingredient {

  private String name;
}
