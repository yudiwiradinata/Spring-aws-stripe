package com.yudi.test.integration;

import com.yudi.backend.persistence.domain.backend.PasswordResetToken;
import com.yudi.backend.persistence.domain.backend.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Yudi on 02/01/2018.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PasswordResetTokenIntegrationTest extends AbstractIntegrationTest{

    @Value("${token.expiration.length.minutes}")
    private int expirationInMinutes;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void init(){
        Assert.assertFalse(expirationInMinutes == 0);
    }

    @Test
    public  void testTokenExpirationLength() throws Exception{
        User user = createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now();
        String token = UUID.randomUUID().toString();

        LocalDateTime expectTime = now.plusMinutes(expirationInMinutes);
        System.out.println(now);
        System.out.println(expectTime);

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);

        LocalDateTime actualTime = passwordResetToken.getExpiryDate();
        Assert.assertNotNull(actualTime);
        Assert.assertEquals(expectTime,actualTime);

    }

    @Test
    public void testFindTokenByTokenValue(){
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        createPasswordResetToken(token, user, now);

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        Assert.assertNotNull(passwordResetToken);
        Assert.assertNotNull(passwordResetToken.getId());
        Assert.assertNotNull(passwordResetToken.getUser());

    }

    @Test
    public void testDeleteToken(){
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
        long tokenId = passwordResetToken.getId();
        passwordResetTokenRepository.delete(tokenId);

        passwordResetToken = passwordResetTokenRepository.findByToken(token);
        Assert.assertNull(passwordResetToken);

    }

    @Test
    public void testCascadeDeleteFromUser(){
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
        passwordResetToken.getId();

        Set<PasswordResetToken> isEmpty = passwordResetTokenRepository.findAllByUserId(user.getId());
        Assert.assertFalse(isEmpty.isEmpty());

        userRepository.delete(user.getId());

        isEmpty = passwordResetTokenRepository.findAllByUserId(user.getId());
        Assert.assertTrue(isEmpty.isEmpty());
    }

    @Test
    public void testMultipleTokensAreReturn() throws Exception{
        User user = createUser(testName);
        LocalDateTime now = LocalDateTime.now();

        String token1 = UUID.randomUUID().toString();
        String token2 = UUID.randomUUID().toString();
        String token3 = UUID.randomUUID().toString();

        Set<PasswordResetToken> tokens = new HashSet<>();
        tokens.add(createPasswordResetToken(token1, user, now));
        tokens.add(createPasswordResetToken(token2, user, now));
        tokens.add(createPasswordResetToken(token3, user, now));

        passwordResetTokenRepository.save(tokens);

        User foundUser = userRepository.findOne(user.getId());

        Set<PasswordResetToken> actualTokens = passwordResetTokenRepository.findAllByUserId(foundUser.getId());
        Assert.assertTrue(actualTokens.size() == tokens.size());

        List<String> tokensAsList = tokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
        List<String> actualTokensAsList = actualTokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());

        Assert.assertEquals(tokensAsList, actualTokensAsList);


    }
}
