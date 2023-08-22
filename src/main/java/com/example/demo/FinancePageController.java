package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinancePageController {

    @GetMapping("/finance")
    public String finance() {
        return "finance";
        }
}
