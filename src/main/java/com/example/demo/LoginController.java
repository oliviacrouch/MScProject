package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    FAQService faqService;

    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "adminpassword";

    @GetMapping("/admin-login")
    public String showLoginPage() {
        return "admin-login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password, Model model) {
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return "redirect:/FAQ-database";
        } else {
            model.addAttribute("error", "Invalid login credentials");
            return "redirect:/admin-login";
        }
    }

    @GetMapping("/FAQ-database")
    public String showAllFAQs(Model model) {
        List<FAQ> allFaqs = faqService.getAllFAQs();
        model.addAttribute("allFaqs", allFaqs);
        return "FAQ-database";
    }

//    @PostMapping("/update-faq")
//    public String updateAnswer(@RequestParam String answer, @RequestParam int id) {
//        faqService.updateAnswer(id, answer);
//        return "faq";
//    }
}