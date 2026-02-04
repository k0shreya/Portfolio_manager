package com.example.portfolio.controller;

import com.example.portfolio.dto.*;
import com.example.portfolio.service.PortfolioItemService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    // ✅ Yahoo symbol search (FIXED)
    @GetMapping("/yahoo/search")
    public ResponseEntity<String> searchYahoo(@RequestParam String q) {

        String url = "https://query1.finance.yahoo.com/v1/finance/search?q="
                + q + "&quotesCount=5&newsCount=0";

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return ResponseEntity.ok(response.getBody());
    }
}
