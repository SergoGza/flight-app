package com.tokioschool.flightapp.store.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class ResourceCreateRequestDTO {

  String description;
}
