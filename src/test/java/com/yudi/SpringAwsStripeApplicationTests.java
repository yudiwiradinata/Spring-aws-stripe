package com.yudi;

import com.yudi.backend.service.I18NService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SpringAwsStripeApplicationTests {

    @Autowired
    private I18NService i18NService;

    @Test
    public void contextLoads() {
        String expect = "Bootstrap starter template";
        String id = "index.main.h1";
        String act = i18NService.getMessage(id);
        Assert.assertEquals("errir", expect, act);

    }

}
