package com.example.portfolio;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {

    private final PortfolioRepository repository;

    public IndexController(PortfolioRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/detail/new")
    public String createNew(Model model) {
        model.addAttribute("item", new PortfolioItem());
        return "form";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PortfolioItem item = repository.findById(id).orElse(null);
        if (item == null) {
            return "redirect:/";
        }
        model.addAttribute("item", item);
        return "view";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        PortfolioItem item = repository.findById(id).orElse(null);
        if (item == null) {
            return "redirect:/";
        }
        model.addAttribute("item", item);
        return "form";
    }
}
