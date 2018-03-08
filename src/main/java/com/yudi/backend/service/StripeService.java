package com.yudi.backend.service;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.*;
import com.yudi.enums.PlansEnum;
import com.yudi.utils.Constans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yudi on 02/02/2018.
 */
@Service
public class StripeService {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(StripeService.class);

    @Autowired
    private String stripeKey;

    public String createSubscription(String custId, String planId) {
        Stripe.apiKey = stripeKey;
        Subscription sub = null;
        try {
            Map<String, Object> item = new HashMap<>();
            item.put("plan", planId);

            Map<String, Object> items = new HashMap<>();
            items.put("0", item);

            Map<String, Object> params = new HashMap<>();
            params.put("customer", custId);
            params.put("items", items);

            sub = Subscription.create(params);
        } catch (AuthenticationException e) {
            LOG.error("error AuthenticationException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (InvalidRequestException e) {
            LOG.error("error InvalidRequestException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (APIConnectionException e) {
            LOG.error("error APIConnectionException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (CardException e) {
            LOG.error("error CardException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (APIException e) {
            LOG.error("error APIException", e);
            throw new com.yudi.exceptions.StripeException(e);
        }

        return sub.getId();
    }


    public String createStripePlan(int planId) {
        Stripe.apiKey = stripeKey;
        // check plan
        Map<String, Object> planParams = new HashMap<String, Object>();
        Plan plan = null;
        try {
            planParams.put("limit", 1);
            PlanCollection planCollection = Plan.list(planParams);
            LOG.info("total plan : " + planCollection.getData().size());
            if (planCollection.getData().size() < 1) {
                planParams.clear();
                planParams.put("id", planId);
                planParams.put("amount", 10);
                planParams.put("interval", "month");
                planParams.put("name", PlansEnum.PRO.getPlanName());
                planParams.put("currency", "usd");

                plan = Plan.create(planParams);
            } else {
                LOG.info("Plan exist");
                for (Plan p : planCollection.getData()) {
                    plan = p;
                    LOG.info("Plan name : " + plan.getName());
                }
            }
        } catch (AuthenticationException e) {
            LOG.error("error AuthenticationException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (InvalidRequestException e) {
            LOG.error("error InvalidRequestException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (APIConnectionException e) {
            LOG.error("error APIConnectionException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (CardException e) {
            LOG.error("error CardException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (APIException e) {
            LOG.error("error APIException", e);
            throw new com.yudi.exceptions.StripeException(e);
        }
        return plan.getId();
    }

    public String createCustomer(Map<String, Object> tokenParams, Map<String, Object> customerParams) {
        Stripe.apiKey = stripeKey;
        String stripeCustId = null;
        try {
            Token token = Token.create(tokenParams);
            customerParams.put(Constans.StripeAttrCustomer.SOURCE, token.getId());
            Customer customer = Customer.create(customerParams);
            stripeCustId = customer.getId();
        } catch (APIConnectionException e) {
            LOG.error("error APIConnectionException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (InvalidRequestException e) {
            LOG.error("error InvalidRequestException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (AuthenticationException e) {
            LOG.error("error AuthenticationException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (APIException e) {
            LOG.error("error APIException", e);
            throw new com.yudi.exceptions.StripeException(e);
        } catch (CardException e) {
            LOG.error("error CardException", e);
            throw new com.yudi.exceptions.StripeException(e);
        }

        return stripeCustId;
    }
}
