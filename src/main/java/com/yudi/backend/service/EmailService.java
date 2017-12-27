package com.yudi.backend.service;

import com.yudi.web.domain.frontend.FeedbackDTO;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by Yudi on 27/12/2017.
 */
public interface EmailService {
    public void sendFeedbackEmail(FeedbackDTO feedbackDTO);

    public void sendGenericEmailMessage(SimpleMailMessage message);

}
