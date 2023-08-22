package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FAQRepository extends JpaRepository<FAQ, Integer> {
    List<FAQ> findByStatus(FAQ.Status status);
}
