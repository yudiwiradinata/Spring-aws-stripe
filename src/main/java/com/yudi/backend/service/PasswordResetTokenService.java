package com.yudi.backend.service;

import com.yudi.backend.persistence.domain.backend.PasswordResetToken;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.persistence.repositories.PasswordResetTokenRepository;
import com.yudi.backend.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by Yudi on 03/01/2018.
 */
@Service
@Transactional(readOnly = true)
public class PasswordResetTokenService {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetTokenService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${token.expiration.length.minutes}")
    private int expirationInMinutes;

    public PasswordResetToken findByToken(String token){
        return passwordResetTokenRepository.findByToken(token);
    }

    @Transactional
    public PasswordResetToken createPasswordResetTokenForEmail(String email){
        PasswordResetToken passwordResetToken = null;
        User user = userRepository.findByEmail(email);
        if(user != null){
            String token = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now();
            passwordResetToken = new PasswordResetToken(token, user, now, expirationInMinutes);
            passwordResetTokenRepository.save(passwordResetToken);
            LOG.debug("Successfully created token {} for user {}", token, user.getUsername());
        }else{
            LOG.warn("we couldn't find a user for the given email {} ", email);
        }

        return passwordResetToken;
    }


}
