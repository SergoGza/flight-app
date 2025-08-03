package com.tokioschool.flightapp.dto;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {

  private UUID resourceId;
  private String filename;
  private String contentType;
  private int size;
}
