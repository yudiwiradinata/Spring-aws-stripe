package com.yudi.test.unit;

import com.yudi.test.integration.StripeIntegrationTest;
import com.yudi.utils.Constans;
import com.yudi.utils.StripeUtils;
import com.yudi.web.domain.frontend.ProAccountPayload;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Yudi on 02/02/2018.
 */
public class StripeUtilsUnitTest {

    @Test
    public void createStripeFromPayload(){
        ProAccountPayload proAccountPayload = new ProAccountPayload();
        String cardNumber = StripeIntegrationTest.TEST_CC_NUMBER;
        proAccountPayload.setCardNumber(cardNumber);
        proAccountPayload.setCardCode(StripeIntegrationTest.TEST_CC_CCV);
        proAccountPayload.setCardMonth(StripeIntegrationTest.TEST_CC_MONTH);
        proAccountPayload.setCardYear(StripeIntegrationTest.TEST_CC_YEAR);

        Map<String, Object> tokenParams = StripeUtils.extractTokenParamFromSignupPayload(proAccountPayload);
        Map<String, Object> cardParams =(Map<String, Object>) tokenParams.get(Constans.StripeAttrToken.CARD);
        Assert.assertEquals(cardNumber, cardParams.get(Constans.StripeAttrToken.CC_NUMBER));

    }
}
