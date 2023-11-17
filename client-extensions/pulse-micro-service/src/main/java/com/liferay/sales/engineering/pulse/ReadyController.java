package com.liferay.sales.engineering.pulse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/ready")
@Controller
public class ReadyController {

    @GetMapping
    @ResponseBody
    public String get() {
        return "READY";
    }

}