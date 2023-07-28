package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FAQController {

    @Autowired
    FAQService faqService;

    @GetMapping("/faq")
    public String faq(Model model) {
        List<FAQ> answeredFAQs = faqService.getAnsweredFAQs();
        model.addAttribute("answeredFAQs", answeredFAQs);
        return "faq";
    }

    @PostMapping("/faq-form")
    public String newFAQ(@RequestParam String question) {
        faqService.createPendingFAQ(question);
        //then return the page which should have the question and its answer on it?
        return "faq";
    }

    @GetMapping("/form")
    public String form() {
        return "form";
    }

}
