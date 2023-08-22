package com.example.demo;

import jakarta.persistence.*;

@Entity
public class FAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="question")
    private String question;
    @Column(name="answer", length = 1000)
    private String answer;
    @Column(name="category")
    @Enumerated(EnumType.STRING)
    private CategoryType category;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public enum Status {
        Pending,
        Answered,
        Rejected
    }

    public enum CategoryType {
        Technical,
        Financial,
        Legal,
        Practical,
        Other
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
