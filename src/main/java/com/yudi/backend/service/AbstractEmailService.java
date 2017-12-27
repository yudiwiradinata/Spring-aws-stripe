package com.yudi.backend.service;

import com.yudi.web.domain.frontend.FeedbackDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by Yudi on 27/12/2017.
 */
public abstract class AbstractEmailService implements EmailService{

    @Value("${default.to.address}")
    private String defaultToAddress;

    protected SimpleMailMessage prepareSimpleMailMessageFromFeedbackDTO(FeedbackDTO feedbackDTO){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(defaultToAddress);
        mailMessage.setFrom(feedbackDTO.getEmail());
        mailMessage.setSubject("Feedback Received from"+feedbackDTO.getFirstName()+" "+feedbackDTO.getLastName());
        mailMessage.setText(feedbackDTO.getFeedback());
        return mailMessage;
    }

    @Override
    public void sendFeedbackEmail(FeedbackDTO feedbackDTO) {
        sendGenericEmailMessage(prepareSimpleMailMessageFromFeedbackDTO(feedbackDTO));
    }
}
