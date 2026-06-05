package com.example.portfolio;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.util.List;

@Controller
public class StatsController {

    private final VisitorStatsRepository repository;

    public StatsController(VisitorStatsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/admin/stats")
    public String statsPage() {
        return "stats"; // Ensure only authenticated users can access via SecurityConfig
    }

    @GetMapping("/api/stats")
    @ResponseBody
    public ResponseEntity<List<VisitorStats>> getStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(7);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();

        List<VisitorStats> stats = repository.findByVisitDateBetweenOrderByVisitDateAsc(start, end);
        return ResponseEntity.ok(stats);
    }
}
