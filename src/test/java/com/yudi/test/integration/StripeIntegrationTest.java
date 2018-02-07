package com.yudi.test.integration;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.yudi.backend.service.StripeService;
import com.yudi.enums.PlansEnum;
import com.yudi.utils.Constans;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yudi on 02/02/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StripeIntegrationTest {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(StripeIntegrationTest.class);

    public static final String TEST_CC_NUMBER = "4242424242424242";
    public static final String TEST_CC_MONTH = "2";
    public static final String TEST_CC_YEAR = "2019";
    public static final String TEST_CC_CCV = "607";


    @Autowired
    private StripeService stripeService;

    @Autowired
    private String stripeKey;

    @Before
    public void init() {
        Assert.assertNotNull(stripeKey);
        Stripe.apiKey = stripeKey;
    }

    @Test
    public void createSubs() throws Exception {
        String planId = stripeService.createStripePlan(PlansEnum.PRO.getId());
        Map<String, Object> tokenParams = new HashMap<>();
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put(Constans.StripeAttrToken.CC_NUMBER, TEST_CC_NUMBER);
        cardParams.put(Constans.StripeAttrToken.CC_MONTH, TEST_CC_MONTH);
        cardParams.put(Constans.StripeAttrToken.CC_YEAR, TEST_CC_YEAR);
        cardParams.put(Constans.StripeAttrToken.CC_CVV, TEST_CC_CCV);
        tokenParams.put(Constans.StripeAttrToken.CARD, cardParams);

        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put(Constans.StripeAttrCustomer.DESCRIPTION, "TES");

        String custId = stripeService.createCustomer(tokenParams, customerParams);

        String subId = stripeService.createSubscription(custId,planId);

    }

    public void createPlan() throws Exception {
        stripeService.createStripePlan(PlansEnum.PRO.getId());
    }

    public void createCustomer() throws Exception {
        Map<String, Object> tokenParams = new HashMap<>();
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put(Constans.StripeAttrToken.CC_NUMBER, TEST_CC_NUMBER);
        cardParams.put(Constans.StripeAttrToken.CC_MONTH, TEST_CC_MONTH);
        cardParams.put(Constans.StripeAttrToken.CC_YEAR, TEST_CC_YEAR);
        cardParams.put(Constans.StripeAttrToken.CC_CVV, TEST_CC_CCV);
        tokenParams.put(Constans.StripeAttrToken.CARD, cardParams);

        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put(Constans.StripeAttrCustomer.DESCRIPTION, "TES");
        //customerParams.put(Constans.StripeAttrCustomer.PLAN, "1");

        String custId = stripeService.createCustomer(tokenParams, customerParams);
        Assert.assertNotNull(custId);

        Customer cu = Customer.retrieve(custId);
        System.out.println(cu.getId());
        cu.delete();


    }


}
