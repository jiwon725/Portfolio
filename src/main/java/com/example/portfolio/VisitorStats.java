package com.example.portfolio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class VisitorStats {
    
    @Id
    private LocalDate visitDate;
    
    private Long visitorCount = 0L; // 방문자수 (UV)
    private Long visitCount = 0L; // 방문횟수 (PV)
    private Long newVisitorCount = 0L; // 신규방문자수
    private Long returningVisitorCount = 0L; // 재방문자수

    // Getters and Setters
    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public Long getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(Long visitorCount) {
        this.visitorCount = visitorCount;
    }

    public Long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }

    public Long getNewVisitorCount() {
        return newVisitorCount;
    }

    public void setNewVisitorCount(Long newVisitorCount) {
        this.newVisitorCount = newVisitorCount;
    }

    public Long getReturningVisitorCount() {
        return returningVisitorCount;
    }

    public void setReturningVisitorCount(Long returningVisitorCount) {
        this.returningVisitorCount = returningVisitorCount;
    }
}
