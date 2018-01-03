package com.yudi.test.integration;

import com.yudi.backend.persistence.domain.backend.Plan;
import com.yudi.backend.persistence.domain.backend.Role;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.persistence.domain.backend.UserRole;
import com.yudi.enums.PlansEnum;
import com.yudi.enums.RolesEnum;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Yudi on 28/12/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest{

    @Test
    public void init() {
        Assert.assertNotNull(userRepository);
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
    }

    @Rule
    public TestName testName = new TestName();

    @Test
    public void testCreateNewRole() {
        Role basicRole = createBasicRole(RolesEnum.BASIC);
        roleRepository.save(basicRole);
        Role getRole = roleRepository.findOne(RolesEnum.BASIC.getId());
        Assert.assertNotNull(getRole);
    }

    @Test
    public void testCreateNewPlan() {
        Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);
        Plan getPlan = planRepository.findOne(basicPlan.getId());
        Assert.assertNotNull(getPlan);
    }

    @Test
    public void testCreateNewUser() {
        /*Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);

        User basicUser = UserUtils.createBasicUser();
        basicUser.setPlan(basicPlan);

        Role basicRole = createBasicRole(RolesEnum.BASIC);
        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser,basicRole);
        *//*userRole.setUser(basicUser);
        userRole.setRole(basicRole);
        *//*
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);

        for (UserRole u : userRoles){
            roleRepository.save(u.getRole());
        }

        basicUser = userRepository.save(basicUser);
        */
        User basicUser = this.createUser();

        User newCreatedUser = userRepository.findOne(basicUser.getId());

        Assert.assertNotNull(newCreatedUser);
        Assert.assertTrue(newCreatedUser.getId() != 0);
        Assert.assertNotNull(newCreatedUser.getPlan());
        Assert.assertNotNull(newCreatedUser.getPlan().getId());

        Set<UserRole> newCreatedUserUserRoles = newCreatedUser.getUserRoles();
        for (UserRole u : newCreatedUserUserRoles){
            Assert.assertNotNull(u.getRole());
            Assert.assertNotNull(u.getRole().getId());
        }

    }

    @Test
    public void testDeleteuser() throws Exception{
        User basicuser = createUser();
        userRepository.delete(basicuser.getId());
    }

    @Test
    public void testFindByEmail() throws Exception{
        User user = createUser(testName);

        user = userRepository.findByEmail(user.getEmail());

        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
    }

    @Test
    public void testUpdateUserPassword() throws Exception{
        User user = createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        System.out.println("old password : "+ user.getPassword());

        String  newPassword = UUID.randomUUID().toString();
        userRepository.updateUserPassword(user.getId(), newPassword);

        user = userRepository.findOne(user.getId());

        System.out.println("new password : "+ user.getPassword());

        Assert.assertEquals(newPassword, user.getPassword());

    }
}
