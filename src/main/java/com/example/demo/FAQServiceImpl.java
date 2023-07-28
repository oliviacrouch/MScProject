package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FAQServiceImpl implements FAQService{

    @Autowired
    FAQRepository faqRepository;

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


}
