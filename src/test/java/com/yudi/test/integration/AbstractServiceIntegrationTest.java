package com.yudi.test.integration;

import com.yudi.backend.persistence.domain.backend.Role;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.persistence.domain.backend.UserRole;
import com.yudi.backend.service.UserService;
import com.yudi.enums.PlansEnum;
import com.yudi.enums.RolesEnum;
import com.yudi.utils.UserUtils;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yudi on 03/01/2018.
 */
public abstract class AbstractServiceIntegrationTest {
    @Autowired
    protected UserService userService;

    protected User createUser(TestName testName) {
        String username = testName.getMethodName();
        String email = testName.getMethodName()+"@iru.com";

        Set<UserRole> userRoles = new HashSet<>();
        User basicUser = UserUtils.createBasicUser(username,email);
        userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));

        return userService.createUser(basicUser, PlansEnum.BASIC, userRoles);
    }
}
