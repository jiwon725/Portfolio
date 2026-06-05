package com.example.portfolio;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final PortfolioRepository repository;

    public ApiController(PortfolioRepository repository) {
        this.repository = repository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        if (repository.count() == 0) {
            saveInitialItem("2026", "스마트 물류 관리 시스템 구축", "C#, .NET Core, SQL Server", "2026.01", "2026.05", "C#");
            saveInitialItem("2025", "전사적 자원 관리(ERP) 차세대 전환", "자바, Spring Boot, React", "2025.03", "2025.11", "자바");
            saveInitialItem("2024", "인사 관리 시스템 고도화", "파워빌더, Oracle DB", "2024.06", "2024.12", "파워빌더");
            saveInitialItem("2023", "대국민 서비스 포털 개편", "자바, JSP, jQuery", "2023.02", "2023.08", "자바");
            saveInitialItem("2022", "사내 회계 정산 프로그램", "C#, WinForms", "2022.07", "2022.10", "C#");
            saveInitialItem("2021", "구형 재고 관리 솔루션 유지보수", "파워빌더, Sybase", "2021.01", "2021.12", "파워빌더");
        }
    }

    private void saveInitialItem(String year, String title, String techStack, String startDate, String endDate, String category) {
        PortfolioItem item = new PortfolioItem();
        item.setYear(year);
        item.setTitle(title);
        item.setTechStack(techStack);
        item.setStartDate(startDate);
        item.setEndDate(endDate);
        item.setCategory(category);
        
        long c = repository.count() + 1;
        String img = "/uploads/portfolio_sample_3.png";
        if(c % 3 == 1) img = "/uploads/portfolio_sample_1.png";
        else if(c % 3 == 2) img = "/uploads/portfolio_sample_2.png";
        
        item.setContent("<p>이 프로젝트는 " + title + "에 대한 상세 설명입니다.</p><p><br></p><p><img src=\"" + img + "\"></p>");
        repository.save(item);
    }

    @GetMapping("/portfolio")
    public List<PortfolioItem> getAll() {
        return repository.findAll();
    }

    @PostMapping("/portfolio")
    public PortfolioItem save(@RequestBody PortfolioItem item) {
        return repository.save(item);
    }

    @DeleteMapping("/portfolio")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids) {
        repository.deleteAllById(ids);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) return ResponseEntity.badRequest().build();
        
        File uploadDir = new File("uploads");
        if (!uploadDir.exists()) uploadDir.mkdir();
        
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            file.transferTo(new File(uploadDir.getAbsolutePath() + "/" + filename));
            return ResponseEntity.ok(Map.of("url", "/uploads/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
