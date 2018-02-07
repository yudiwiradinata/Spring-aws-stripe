package com.yudi.utils;

import com.yudi.web.domain.frontend.ProAccountPayload;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yudi on 02/02/2018.
 */
public class StripeUtils {

    public static Map<String, Object> extractTokenParamFromSignupPayload(ProAccountPayload payload){
        Map<String, Object> tokenParams = new HashMap<>();
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put(Constans.StripeAttrToken.CC_NUMBER, payload.getCardNumber());
        cardParams.put(Constans.StripeAttrToken.CC_MONTH, Integer.valueOf(payload.getCardMonth()));
        cardParams.put(Constans.StripeAttrToken.CC_YEAR, Integer.valueOf(payload.getCardYear()));
        cardParams.put(Constans.StripeAttrToken.CC_CVV, payload.getCardCode());
        tokenParams.put(Constans.StripeAttrToken.CARD, cardParams);
        return tokenParams;
    }
}
