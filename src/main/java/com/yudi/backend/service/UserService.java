package com.yudi.backend.service;

import com.yudi.backend.persistence.domain.backend.Plan;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.persistence.domain.backend.UserRole;
import com.yudi.backend.persistence.repositories.PlanRepository;
import com.yudi.backend.persistence.repositories.RoleRepository;
import com.yudi.backend.persistence.repositories.UserRepository;
import com.yudi.enums.PlansEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by Yudi on 28/12/2017.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findById(long id){
        return userRepository.findOne(id);
    }

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles){
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        Plan plan = new Plan(plansEnum);
        if(!planRepository.exists(plansEnum.getId())){
            plan = planRepository.save(plan);
        }

        user.setPlan(plan);

        for (UserRole u : userRoles){
            roleRepository.save(u.getRole());
        }

        user.getUserRoles().addAll(userRoles);

        user = userRepository.save(user);

        return user;
    }

    @Transactional
    public void updateUserPassword(long id, String password){
        password = passwordEncoder.encode(password);
        userRepository.updateUserPassword(id, password);
        LOG.debug("Password updated succsessfully for user id {}", id);

    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }


}
