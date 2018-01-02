package com.yudi.test.integration;

import com.yudi.backend.persistence.domain.backend.Plan;
import com.yudi.backend.persistence.domain.backend.Role;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.persistence.domain.backend.UserRole;
import com.yudi.backend.persistence.repositories.PlanRepository;
import com.yudi.backend.persistence.repositories.RoleRepository;
import com.yudi.backend.persistence.repositories.UserRepository;
import com.yudi.enums.PlansEnum;
import com.yudi.enums.RolesEnum;
import com.yudi.utils.UserUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yudi on 28/12/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RepositoriesIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void init() {
        Assert.assertNotNull(userRepository);
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
    }

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

    private Role createBasicRole(RolesEnum rolesEnum) {
        /*Role role = new Role();
        role.setId(BASIC_ROLE_ID);
        role.setName("ROLE_USER");
        return role;*/
        return new Role(rolesEnum);
    }

    private Plan createBasicPlan(PlansEnum plansEnum) {
        /*Plan plan = new Plan();
        plan.setId(BASIC_PLAN_ID);
        plan.setName("Basic");
        return plan;*/
        return new Plan(plansEnum);
    }

    private User createUser(){
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
}
