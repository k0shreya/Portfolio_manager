package com.example.portfolio.controller;

import com.example.portfolio.dto.*;
import com.example.portfolio.service.PortfolioItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/portfolio-items")
public class PortfolioItemController {

    private final PortfolioItemService portfolioItemService;

    public PortfolioItemController(PortfolioItemService portfolioItemService) {
        this.portfolioItemService = portfolioItemService;
    }
    @PostMapping("/buy")
    public void buy(@RequestBody BuyRequest request) {
        portfolioItemService.buyAsset(request);
    }

    @PostMapping("/sell")
    public void sell(@RequestBody SellRequest request) {
        portfolioItemService.sellAsset(request);
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {
        return portfolioItemService.getDashboard();
    }

    @GetMapping("/cash")
    public CashResponse getCash() {
        return portfolioItemService.getCash();
    }

    @PutMapping("/cash")
    public CashResponse updateCash(@RequestBody CashUpdateRequest request) {
        return portfolioItemService.updateCash(request);
    }

}
