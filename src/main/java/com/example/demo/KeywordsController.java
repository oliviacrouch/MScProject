package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KeywordsController {
    @GetMapping("/keywords")
    public String keywords() {
        return "keywords";
    }
}
