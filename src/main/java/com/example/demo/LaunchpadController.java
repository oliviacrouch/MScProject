package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LaunchpadController {
    @GetMapping("/launchpad")
    public String launchpad(){
        return "launchpad";
    }
}
