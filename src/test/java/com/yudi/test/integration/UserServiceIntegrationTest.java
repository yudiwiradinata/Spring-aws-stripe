package com.yudi.test.integration;

import com.yudi.backend.persistence.domain.backend.User;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by Yudi on 28/12/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Rule
    public TestName testName = new TestName();

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void testCreateUser() throws Exception{
        User user = createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        System.out.println(user.getId());

    }

    @Test
    public void testUpdateUserPassword() throws Exception{
        User user = createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        System.out.println("old password : "+ user.getPassword());

        String  newPassword = UUID.randomUUID().toString();
        userService.updateUserPassword(user.getId(), newPassword);

        user = userService.findById(user.getId());

        System.out.println("new password : "+ user.getPassword());

        Assert.assertTrue(passwordEncoder.matches(newPassword, user.getPassword()));

    }

}
