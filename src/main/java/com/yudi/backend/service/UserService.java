package com.yudi.backend.service;

import com.yudi.backend.persistence.domain.backend.Plan;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.persistence.domain.backend.UserRole;
import com.yudi.backend.persistence.repositories.PlanRepository;
import com.yudi.backend.persistence.repositories.RoleRepository;
import com.yudi.backend.persistence.repositories.UserRepository;
import com.yudi.enums.PlansEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by Yudi on 28/12/2017.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles){
        Plan plan = new Plan(plansEnum);

        if(!planRepository.exists(plansEnum.getId())){
            plan = planRepository.save(plan);
        }

        for (UserRole u : userRoles){
            roleRepository.save(u.getRole());
        }

        user.getUserRoles().addAll(userRoles);

        user = userRepository.save(user);

        return user;
    }

}
