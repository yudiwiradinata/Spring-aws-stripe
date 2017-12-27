package com.yudi.web.controllers;

import com.yudi.utils.Constans;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Yudi on 27/12/2017.
 */
@Controller
public class LoginController {

    @RequestMapping(value = Constans.Login.LOGIN_LINK)
    public String login(){
        return Constans.Login.LOGIN_VIEW_NAME;
    }

}
