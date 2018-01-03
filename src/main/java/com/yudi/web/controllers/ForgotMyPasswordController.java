package com.yudi.web.controllers;

import com.yudi.backend.persistence.domain.backend.PasswordResetToken;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.service.EmailService;
import com.yudi.backend.service.I18NService;
import com.yudi.backend.service.PasswordResetTokenService;
import com.yudi.backend.service.UserService;
import com.yudi.utils.Constans;
import com.yudi.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Locale;

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

    @Autowired
    private UserService userService;

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


    @RequestMapping(value = Constans.ForgotPassword.CHANGE_PASSWORD_PATH, method = RequestMethod.GET)
    public String changePasswordGet(@RequestParam("id") long id,
                                    @RequestParam("token") String token,
                                    Locale locale,
                                    ModelMap model){

        if(StringUtils.isEmpty(token) || id == 0){
            LOG.error("Invalid user id {} or token value {}", id, token);
            model.addAttribute(Constans.ForgotPassword.PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(Constans.ForgotPassword.MESSAGE_ATTRIBUTE_NAME, "Invalid user id or token value");
            return Constans.ForgotPassword.CHANGE_PASSWORD_VIEW_NAME;
        }

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);

        if(passwordResetToken == null){
            LOG.warn("A token couldn't be found with value {}",token);
            model.addAttribute(Constans.ForgotPassword.PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(Constans.ForgotPassword.MESSAGE_ATTRIBUTE_NAME, "Token not found");
            return Constans.ForgotPassword.CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = passwordResetToken.getUser();
        if(user.getId() != id){
            LOG.error("The user id {} passed as parameter does not match the user id {} associated with token {}",id, user.getId(), token);
            model.addAttribute(Constans.ForgotPassword.PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(Constans.ForgotPassword.MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.invalid", locale));
            return Constans.ForgotPassword.CHANGE_PASSWORD_VIEW_NAME;
        }

        if(LocalDateTime.now().isAfter(passwordResetToken.getExpiryDate())){
            LOG.error("Token {} has expired", token);
            model.addAttribute(Constans.ForgotPassword.PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(Constans.ForgotPassword.MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.expired", locale));
            return Constans.ForgotPassword.CHANGE_PASSWORD_VIEW_NAME;
        }

        model.addAttribute("principalId", user.getId());

        // OK to process, we auto authenticate the user, so that in POST request we can check if the user
        // is authenticated
        Authentication auth = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return Constans.ForgotPassword.CHANGE_PASSWORD_VIEW_NAME;
    }


    @RequestMapping(value = Constans.ForgotPassword.CHANGE_PASSWORD_PATH, method = RequestMethod.POST)
    public String changePasswordPost(@RequestParam("principal_id") long id,
                                    @RequestParam("password") String password,
                                    ModelMap model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            LOG.error("An authenticated user tried to invoke the reset password POST method");
            model.addAttribute(Constans.ForgotPassword.PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(Constans.ForgotPassword.MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this request");
            return Constans.ForgotPassword.CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = (User) authentication.getPrincipal();
        if(user.getId() != id){
            LOG.error("Security breach! user {} is trying to make password reset request on behalf of {}", user.getId(), id);
            model.addAttribute(Constans.ForgotPassword.PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(Constans.ForgotPassword.MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this request");
            return Constans.ForgotPassword.CHANGE_PASSWORD_VIEW_NAME;
        }

        userService.updateUserPassword(id,password);
        LOG.info("Password successfully updated for user {}", user.getUsername());

        model.addAttribute(Constans.ForgotPassword.PASSWORD_RESET_ATTRIBUTE_NAME, "true");
        return Constans.ForgotPassword.CHANGE_PASSWORD_VIEW_NAME;

    }
}
