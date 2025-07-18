package com.tokioschool.flightapp.store.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class ResourceContentDTO {

    UUID resourceId;
    byte[] content;
    String contentType;
    String filename;
    String description;
    int size;

}
