package com.tokioschool.flightapp.store.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ResourceIdDTO {

  UUID resourceId;
}
