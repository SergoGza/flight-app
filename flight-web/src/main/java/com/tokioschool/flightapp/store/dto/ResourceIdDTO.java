package com.tokioschool.flightapp.store.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ResourceIdDTO {

  UUID resourceId;
}
