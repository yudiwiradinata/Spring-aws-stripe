package com.yudi.web.controllers;

import com.yudi.backend.service.EmailService;
import com.yudi.utils.Constans;
import com.yudi.web.domain.frontend.FeedbackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Yudi on 27/12/2017.
 */
@Controller
public class ContactController {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = Constans.Contact.CONTACT_US_LINK, method = RequestMethod.GET)
    public String contactGet(ModelMap model){
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        model.addAttribute(Constans.Contact.FEEDBACK_MODEL_KEY, feedbackDTO);
        return Constans.Contact.CONTACT_US_VIEW_NAME;
    }

    @RequestMapping(value = Constans.Contact.CONTACT_US_LINK, method = RequestMethod.POST)
    public String contactPost(@ModelAttribute(Constans.Contact.FEEDBACK_MODEL_KEY) FeedbackDTO feedback){
        LOG.debug("feedback model {}",feedback);
        emailService.sendFeedbackEmail(feedback);
        return Constans.Contact.CONTACT_US_VIEW_NAME;
    }

}
