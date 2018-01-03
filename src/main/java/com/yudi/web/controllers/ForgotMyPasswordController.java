package com.yudi.web.controllers;

import com.yudi.backend.persistence.domain.backend.PasswordResetToken;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.service.EmailService;
import com.yudi.backend.service.PasswordResetTokenService;
import com.yudi.utils.Constans;
import com.yudi.utils.UserUtils;
import com.yudi.backend.service.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Yudi on 03/01/2018.
 */
@Controller
public class ForgotMyPasswordController {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Value("${webmaster.email}")
    private String webMasterEmail;

    @RequestMapping(value = Constans.ForgotPassword.FORGOT_PASSWORD_LINK, method = RequestMethod.GET)
    public String forgotPasswordGet(){
        return Constans.ForgotPassword.EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = Constans.ForgotPassword.FORGOT_PASSWORD_LINK, method = RequestMethod.POST)
    public String forgotPasswordPost(HttpServletRequest request, @RequestParam("email") String email,
                                     ModelMap model){
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);

        if(passwordResetToken == null){
            LOG.warn("Couldn't find a  password reset token for email {}", email);
        } else {
            LOG.info("Token {}", passwordResetToken.getToken());
            LOG.info("Username : {}", passwordResetToken.getUser().getUsername());

            User user = passwordResetToken.getUser();
            String token = passwordResetToken.getToken();

            String passwordResetUrl = UserUtils.createPasswordResetURL(request, user.getId(), token);
            LOG.debug("passwordResetUrl {}", passwordResetUrl);

            String emailText = i18NService.getMessage(Constans.ForgotPassword.EMAIL_MESSAGE_TEXT_PROPERTY_NAME, request.getLocale());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("[yudi tes]: How To Reset Password");
            mailMessage.setText(emailText + "\r\n" + passwordResetUrl);
            mailMessage.setFrom(webMasterEmail);

            emailService.sendGenericEmailMessage(mailMessage);
        }

        model.addAttribute(Constans.ForgotPassword.MAIL_SENT_KEY, "true");

        return Constans.ForgotPassword.EMAIL_ADDRESS_VIEW_NAME;
    }



}
