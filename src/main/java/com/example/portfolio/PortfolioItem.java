package com.example.portfolio;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class PortfolioItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "project_year")
    private String year;
    
    private String title;
    private String techStack;
    private String startDate;
    private String endDate;
    private String category;
    
    @Column(columnDefinition = "TEXT")
    private String content;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTechStack() { return techStack; }
    public void setTechStack(String techStack) { this.techStack = techStack; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
