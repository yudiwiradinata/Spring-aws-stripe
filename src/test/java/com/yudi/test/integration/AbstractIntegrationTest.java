package com.yudi.test.integration;

import com.yudi.backend.persistence.domain.backend.*;
import com.yudi.backend.persistence.repositories.PasswordResetTokenRepository;
import com.yudi.backend.persistence.repositories.PlanRepository;
import com.yudi.backend.persistence.repositories.RoleRepository;
import com.yudi.backend.persistence.repositories.UserRepository;
import com.yudi.enums.PlansEnum;
import com.yudi.enums.RolesEnum;
import com.yudi.utils.UserUtils;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yudi on 02/01/2018.
 */
public abstract class AbstractIntegrationTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PlanRepository planRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected PasswordResetTokenRepository passwordResetTokenRepository;

    protected Role createBasicRole(RolesEnum rolesEnum) {
        /*Role role = new Role();
        role.setId(BASIC_ROLE_ID);
        role.setName("ROLE_USER");
        return role;*/
        return new Role(rolesEnum);
    }

    protected Plan createBasicPlan(PlansEnum plansEnum) {
        /*Plan plan = new Plan();
        plan.setId(BASIC_PLAN_ID);
        plan.setName("Basic");
        return plan;*/
        return new Plan(plansEnum);
    }

    protected User createUser(){
        Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);

        User basicUser = UserUtils.createBasicUser();
        basicUser.setPlan(basicPlan);

        Role basicRole = createBasicRole(RolesEnum.BASIC);
        roleRepository.save(basicRole);

        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser,basicRole);
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);
        basicUser = userRepository.save(basicUser);
        return basicUser;
    }

    protected User createUser(String username, String email){
        Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);

        User basicUser = UserUtils.createBasicUser(username,email);
        basicUser.setPlan(basicPlan);

        Role basicRole = createBasicRole(RolesEnum.BASIC);
        roleRepository.save(basicRole);

        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser,basicRole);
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);
        basicUser = userRepository.save(basicUser);
        return basicUser;
    }

    protected User createUser(TestName testName){
        return createUser(testName.getMethodName(), testName.getMethodName() + "@da.com");
    }

    protected PasswordResetToken createPasswordResetToken(String token, User user, LocalDateTime now){
        PasswordResetToken passwordResetToken = new PasswordResetToken(token,user,now,0);
        return passwordResetTokenRepository.save(passwordResetToken);
    }
}
