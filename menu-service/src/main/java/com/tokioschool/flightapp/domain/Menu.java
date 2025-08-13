package com.tokioschool.flightapp.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString // Solo para la prueba de concepto
@Document(collection = "menus")
public class Menu {

  @Id private String id;
  private Instant created;
  private String title;
  private boolean vegetarian;
  private List<Main> mains;

  @Field(targetType = FieldType.DECIMAL128)
  private BigDecimal calories;

  @DBRef private Beer beer;
}
