package com.yudi.web.controllers;

import com.yudi.utils.Constans;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Yudi on 27/12/2017.
 */
@Controller
public class PayloadController {

    @RequestMapping(Constans.Payload.PAYLOAD_LINK)
    public String payload(){
        return Constans.Payload.PAYLOAD_VIEW_NAME;
    }
}
