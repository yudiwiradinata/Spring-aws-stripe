package com.yudi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Yudi on 21/12/2017.
 */
@Controller
public class HelloWorldController {

    @RequestMapping("/")
    public String sayHello(){
        return "index";
    }

}
