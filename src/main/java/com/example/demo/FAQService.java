package com.example.demo;

import java.util.List;
import java.util.Map;

public interface FAQService {
    List<FAQ> getAnsweredFAQs();
    FAQ createPendingFAQ(String question);
    Map<FAQ.CategoryType, List<FAQ>> sortFAQsByCategory();
}
