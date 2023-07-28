package com.example.demo;

import java.util.List;

public interface FAQService {
    List<FAQ> getAnsweredFAQs();
    FAQ createPendingFAQ(String question);
}
