package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LearnController {

    @GetMapping("/learn-launchpad")
    public String learnLaunchpad() {
        return "learn-launchpad";
    }
}
