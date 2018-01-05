package com.yudi.web.controllers;

import com.yudi.enums.PlansEnum;
import com.yudi.utils.Constans;
import com.yudi.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Yudi on 04/01/2018.
 */
@Controller
public class SignupController {

    /*the application logger*/
    private static final Logger LOG = LoggerFactory.getLogger(SignupController.class);

    @RequestMapping(value = Constans.SignUp.SIGNUP_URL_MAPPING, method = RequestMethod.GET)
    public String signupGet(@RequestParam("planId") int planId, ModelMap model){

        if(planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()){
            throw new IllegalArgumentException("Plan is not valid");
        }

        model.addAttribute(Constans.SignUp.PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());

        return Constans.SignUp.SUBSCRIPTION_VIEW_NAME;
    }
}
