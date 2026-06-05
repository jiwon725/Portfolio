package com.example.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface VisitorStatsRepository extends JpaRepository<VisitorStats, LocalDate> {
    List<VisitorStats> findByVisitDateBetweenOrderByVisitDateAsc(LocalDate startDate, LocalDate endDate);
}
