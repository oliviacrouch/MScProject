package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FAQServiceImpl implements FAQService{

    //Map<FAQ.CategoryType, List<FAQ>> faqsByCategory = new HashMap<>();

    @Autowired
    FAQRepository faqRepository;

    @Override
    public List<FAQ> getAllFAQs () {
        return faqRepository.findAll();
    }

    @Override
    public FAQ updateAnswer(int id, String answer) {
        FAQ faq = faqRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("FAQ not found."));
        faq.setAnswer(answer);
        faq.setStatus(FAQ.Status.Answered);
        return faqRepository.save(faq);
    }

    @Override
    public List<FAQ> getAnsweredFAQs() {
        return faqRepository.findByStatus(FAQ.Status.Answered);
    }

    @Override
    public FAQ createPendingFAQ(String question) {
        FAQ faq = new FAQ();
        faq.setQuestion(question);
        faq.setAnswer("Pending Admin Response");
        faq.setStatus(FAQ.Status.valueOf("Pending"));
        
        if (question.contains("API") || question.contains("algorithm") || question.contains("calculation")
        || question.contains("api") || question.contains("algorithms") || question.contains("calculate") ||
        question.contains("calculates") || question.contains("reliable") || question.contains("reliability") ||
        question.contains("personal data")) {
            faq.setCategory(FAQ.CategoryType.valueOf("Technical"));
        } else if (question.contains("legal") || question.contains("Legal") || question.contains("advice")) {
            faq.setCategory(FAQ.CategoryType.valueOf("Legal"));
        } else if (question.contains("BTL") || question.contains("taxation") || question.contains("mortgage") ||
                question.contains("down payment") || question.contains("interest rate") || question.contains("cost for " +
                "comparison")) {
            faq.setCategory(FAQ.CategoryType.valueOf("Financial"));
        } else if (question.contains("web app") || question.contains("application") && question.contains("start")
            || question.contains("order") || question.contains("purpose")) {
            faq.setCategory(FAQ.CategoryType.valueOf("Practical"));
        } else {
            faq.setCategory(FAQ.CategoryType.valueOf("Other"));
        }
        return faqRepository.save(faq);
    }

    @Override
    public Map<FAQ.CategoryType, List<FAQ>> sortFAQsByCategory() {
        Map<FAQ.CategoryType, List<FAQ>> faqsByCategory = new HashMap<>();
        for (FAQ faq : getAnsweredFAQs()) {
            FAQ.CategoryType categoryType = faq.getCategory();
            faqsByCategory.computeIfAbsent(categoryType, k -> new ArrayList<>()).add(faq);
        }
        return faqsByCategory;
    }

}
