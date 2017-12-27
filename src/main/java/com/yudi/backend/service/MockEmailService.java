package com.yudi.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by Yudi on 27/12/2017.
 */
public class MockEmailService extends AbstractEmailService {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage message) {
        LOG.debug("Simulate an email  service");
        LOG.info(message.toString());
        LOG.debug("email sent");
    }

}
