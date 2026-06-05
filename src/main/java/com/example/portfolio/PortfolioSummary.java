package com.example.portfolio;

public interface PortfolioSummary {
    Long getId();
    String getYear();
    String getTitle();
    String getTechStack();
    String getStartDate();
    String getEndDate();
    String getCategory();
    // 'content' (Base64 이미지 데이터)는 가져오지 않음으로써 속도 최적화
}
