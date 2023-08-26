package com.example.demo;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class FAQController {

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

    @ExceptionHandler(PSQLException.class)
    public String psqlException(PSQLException psqlException) {
        return "faq-submit-error";
    }

    @GetMapping("/FAQ-database")
    public String showAllFAQs(Model model) {
        List<FAQ> allFaqs = faqService.getAllFAQs();
        model.addAttribute("allFaqs", allFaqs);
        return "FAQ-database";
    }

    @GetMapping("/faq")
    public String faq(@RequestParam(name = "category", defaultValue = "Practical") String category, Model model) {
        List<FAQ> answeredFAQs = faqService.getAnsweredFAQs();
        model.addAttribute("answeredFAQs", answeredFAQs);
        Map<FAQ.CategoryType, List<FAQ>> faqsByCategory = faqService.sortFAQsByCategory();
        model.addAttribute("faqsByCategory", faqsByCategory);
        model.addAttribute("selectedCategory", category);
        return "faq";
    }

    @PostMapping("/faq-form")
    public String newFAQ(@RequestParam String question, Model model) {
        FAQ newFaq = faqService.createPendingFAQ(question);
        List<FAQ> answeredFAQs = faqService.getAnsweredFAQs();
        model.addAttribute("answeredFAQs", answeredFAQs);
        Map<FAQ.CategoryType, List<FAQ>> faqsByCategory = faqService.sortFAQsByCategory();
        model.addAttribute("faqsByCategory", faqsByCategory);
        model.addAttribute("selectedCategory", "Practical"); // Set the default category
        model.addAttribute("newFaq", newFaq); // Add the newly created FAQ to the model
        return "faq";
    }


    @GetMapping("/form")
    public String form() {
        return "form";
    }

    @PostMapping("/update-faq")
    public String updateAnswer(@RequestParam String answer, @RequestParam int id, Model model) {
        FAQ newAnswer = faqService.updateAnswer(id, answer);
        model.addAttribute("answer", newAnswer);
        return "redirect:/faq";
    }

    @PostMapping("/delete-faq")
    public String deleteFAQ(@RequestParam int id, Model model) {
        faqService.deleteFAQ(id);
        return "redirect:/faq";
    }

}
