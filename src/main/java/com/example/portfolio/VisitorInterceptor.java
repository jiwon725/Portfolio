package com.example.portfolio;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class VisitorInterceptor implements HandlerInterceptor {

    private final VisitorStatsRepository repository;

    public VisitorInterceptor(VisitorStatsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LocalDate today = LocalDate.now();
        
        // Find or create today's stats
        VisitorStats stats = repository.findById(today).orElseGet(() -> {
            VisitorStats newStats = new VisitorStats();
            newStats.setVisitDate(today);
            return newStats;
        });

        // 1. Increment Page View (visitCount)
        stats.setVisitCount(stats.getVisitCount() + 1);

        // 2. Check cookies for Unique Visitors
        Cookie[] cookies = request.getCookies();
        boolean hasVisitorId = false;
        boolean hasVisitedToday = false;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("visitor_id".equals(c.getName())) hasVisitorId = true;
                if ("visited_today".equals(c.getName())) hasVisitedToday = true;
            }
        }

        if (!hasVisitedToday) {
            stats.setVisitorCount(stats.getVisitorCount() + 1);
            
            if (!hasVisitorId) {
                stats.setNewVisitorCount(stats.getNewVisitorCount() + 1);
                
                Cookie visitorIdCookie = new Cookie("visitor_id", UUID.randomUUID().toString());
                visitorIdCookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
                visitorIdCookie.setPath("/");
                response.addCookie(visitorIdCookie);
            } else {
                stats.setReturningVisitorCount(stats.getReturningVisitorCount() + 1);
            }

            Cookie visitedTodayCookie = new Cookie("visited_today", "true");
            visitedTodayCookie.setMaxAge(60 * 60 * 24); // 24 hours (simplified)
            visitedTodayCookie.setPath("/");
            response.addCookie(visitedTodayCookie);
        }

        repository.save(stats);
        return true;
    }
}
