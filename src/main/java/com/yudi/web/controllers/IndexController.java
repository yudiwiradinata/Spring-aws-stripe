package com.yudi.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Yudi on 22/12/2017.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String home(){
        return "index";
    }
}
