package com.example.portfolio.dto;

import lombok.Data;
import java.util.List;

@Data
public class YahooResponse {

    private Chart chart;

    @Data
    public static class Chart {
        private List<Result> result;
    }

    @Data
    public static class Result {
        private Meta meta;
    }

    @Data
    public static class Meta {
        private double regularMarketPrice;
    }
}
