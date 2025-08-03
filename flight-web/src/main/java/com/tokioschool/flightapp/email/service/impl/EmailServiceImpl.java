package com.tokioschool.flightapp.email.service.impl;

import com.tokioschool.flightapp.email.dto.EmailDTO;
import com.tokioschool.flightapp.email.service.EmailService;
import jakarta.activation.DataHandler;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;

  @Override
  public void sendEmail(EmailDTO emailDTO) {

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    try {

      mimeMessage.addRecipients(Message.RecipientType.TO, emailDTO.getTo());
      mimeMessage.setSubject(emailDTO.getSubject(), StandardCharsets.UTF_8.name());

      Multipart multipart = new MimeMultipart();
      mimeMessage.setContent(multipart);

      MimeBodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setText(emailDTO.getTextBody(), StandardCharsets.UTF_8.name());
      multipart.addBodyPart(messageBodyPart);

      emailDTO
          .getAttachments()
          .forEach(
              attachmentDTO -> {
                try {
                  MimeBodyPart mimeBodyPart = new MimeBodyPart();
                  mimeBodyPart.setDataHandler(
                      new DataHandler(
                          new ByteArrayDataSource(
                              attachmentDTO.getContent(), attachmentDTO.getContentType())));
                  mimeBodyPart.setFileName(attachmentDTO.getFilename());
                  multipart.addBodyPart(mimeBodyPart);

                } catch (MessagingException me) {
                  log.error(
                      "Exception when adding attachment to mimeMessage, will be ifnored, to:{}, subject:{}",
                      emailDTO.getTo(),
                      emailDTO.getSubject());
                }
              });

    } catch (MessagingException me) {
      log.error(
          "Exception when building mimeMessage, to:{} subject:{}",
          emailDTO.getTo(),
          emailDTO.getSubject(),
          me);
      return;
    }
    try {
      javaMailSender.send(mimeMessage);
    } catch (MailException me) {
      log.error(
          "Exception when sending mimeMessage, to:{}, subject:{}",
          emailDTO.getTo(),
          emailDTO.getSubject(),
          me);
    }
  }
}
