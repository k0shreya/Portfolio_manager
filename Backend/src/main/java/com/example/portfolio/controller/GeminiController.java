package com.example.portfolio.controller;

import com.example.portfolio.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        return geminiService.askGemini(message);
    }
}