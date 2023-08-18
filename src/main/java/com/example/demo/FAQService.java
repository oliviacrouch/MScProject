package com.example.demo;

import java.util.List;
import java.util.Map;

public interface FAQService {
    List<FAQ> getAllFAQs();
    FAQ updateAnswer(int id, String answer);
    List<FAQ> getAnsweredFAQs();
    FAQ createPendingFAQ(String question);
    Map<FAQ.CategoryType, List<FAQ>> sortFAQsByCategory();
    void deleteFAQ(int id);
}
