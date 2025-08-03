package com.tokioschool.flightapp.email.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmailDTO {

  String to;
  String subject;
  String textBody;
  List<AttachmentDTO> attachments;
}
