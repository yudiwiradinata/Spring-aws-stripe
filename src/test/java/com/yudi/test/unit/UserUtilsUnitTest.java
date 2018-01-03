package com.yudi.test.unit;

import com.yudi.utils.Constans;
import com.yudi.utils.UserUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.UUID;

/**
 * Created by Yudi on 03/01/2018.
 */
public class UserUtilsUnitTest {

    private MockHttpServletRequest mockHttpServletRequest;

    @Before
    public void init(){
        mockHttpServletRequest = new MockHttpServletRequest();
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
