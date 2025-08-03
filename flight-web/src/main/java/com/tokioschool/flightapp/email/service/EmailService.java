package com.tokioschool.flightapp.email.service;

import com.tokioschool.flightapp.email.dto.EmailDTO;

public interface EmailService {
    void sendEmail(EmailDTO emailDTO);
}
