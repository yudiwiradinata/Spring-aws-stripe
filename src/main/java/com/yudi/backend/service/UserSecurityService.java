package com.yudi.backend.service;

import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Yudi on 02/01/2018.
 */
@Service
public class UserSecurityService implements UserDetailsService {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);
        if(null == user){
           LOG.warn("username not found {}", s);
            throw  new UsernameNotFoundException("username not found");
        }
        return user;
    }
}
