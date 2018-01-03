package com.yudi.test.integration;

import com.yudi.backend.persistence.domain.backend.PasswordResetToken;
import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.backend.service.PasswordResetTokenService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Yudi on 03/01/2018.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PasswordResetTokenServiceIntegrationTest extends AbstractServiceIntegrationTest{

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Rule
    public TestName testName = new TestName();

    @Test
    public void testCreateNewTokenForEmail(){
        User user = createUser(testName);
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(user.getEmail());
        Assert.assertNotNull(passwordResetToken);
        Assert.assertNotNull(passwordResetToken.getToken());
    }

    @Test
    public void testFindByToken(){
        User user = createUser(testName);
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(user.getEmail());
        Assert.assertNotNull(passwordResetToken);
        Assert.assertNotNull(passwordResetToken.getToken());

        PasswordResetToken token = passwordResetTokenService.findByToken(passwordResetToken.getToken());
        Assert.assertNotNull(token);
    }
}
