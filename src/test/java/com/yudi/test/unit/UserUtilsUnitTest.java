package com.yudi.test.unit;

import com.yudi.backend.persistence.domain.backend.User;
import com.yudi.utils.Constans;
import com.yudi.utils.UserUtils;
import com.yudi.web.domain.frontend.BasicAccountPayload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.UUID;

/**
 * Created by Yudi on 03/01/2018.
 */
public class UserUtilsUnitTest {

    private MockHttpServletRequest mockHttpServletRequest;

    private PodamFactory podamFactory;

    @Before
    public void init(){
        mockHttpServletRequest = new MockHttpServletRequest();
        podamFactory = new PodamFactoryImpl();
    }

    @Test
    public void mapWebUserToDomainUser() throws Exception {
        BasicAccountPayload webUser = podamFactory.manufacturePojoWithFullData(BasicAccountPayload.class);
        webUser.setEmail("me@example.com");

        User user = UserUtils.fromWebUserToDomainUser(webUser);

        Assert.assertNotNull(user);

        Assert.assertEquals(webUser.getUsername(), user.getUsername());
        Assert.assertEquals(webUser.getPassword(), user.getPassword());
        Assert.assertEquals(webUser.getFirstName(), user.getFirstName());
        Assert.assertEquals(webUser.getUsername(), user.getUsername());
        Assert.assertEquals(webUser.getLastName(), user.getLastName());
        Assert.assertEquals(webUser.getEmail(), user.getEmail());
        Assert.assertEquals(webUser.getPhoneNumber(), user.getPhoneNumber());
        Assert.assertEquals(webUser.getDescription(), user.getDescription());
        Assert.assertEquals(webUser.getCountry(), user.getCountry());

    }

        @Test
    public void testPasswordResetEmail() throws Exception{
        mockHttpServletRequest.setServerPort(8080); //default is 80

        String token = UUID.randomUUID().toString();
        long userId = 2;

        String expectedURL ="http://localhost:8080"+
                Constans.ForgotPassword.CHANGE_PASSWORD_PATH+"?id="+userId+"&token="+token;

        String actualURL = UserUtils.createPasswordResetURL(mockHttpServletRequest, userId, token);

        Assert.assertEquals(expectedURL, actualURL);

    }
}
