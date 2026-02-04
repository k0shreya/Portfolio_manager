package com.example.portfolio.service;

import com.example.portfolio.beans.*;
import com.example.portfolio.dto.*;
import com.example.portfolio.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class PortfolioItemServiceImp implements PortfolioItemService {

    private final PortfolioItemRepository portfolioItemRepository;
    private final CashBalanceRepository cashBalanceRepository;
    private final TransactionLogRepository transactionLogRepository;
    private final RestTemplate restTemplate;

    // ---------- CASH ----------

    private CashBalance getOrCreateCash() {
        return cashBalanceRepository.findById(1)
                .orElseGet(() ->
                        cashBalanceRepository.save(
                                CashBalance.builder()
                                        .id(1)
                                        .balance(0.0)
                                        .build()
                        )
                );
    }

    @Override
    public CashResponse getCash() {
        return CashResponse.builder()
                .cashBalance(getOrCreateCash().getBalance())
                .build();
    }

    @Override
    public CashResponse updateCash(CashUpdateRequest request) {
        CashBalance cash = getOrCreateCash();
        cash.setBalance(cash.getBalance() + request.getAmount());
        cashBalanceRepository.save(cash);

        return CashResponse.builder()
                .cashBalance(cash.getBalance())
                .build();
    }

    // ---------- BUY ----------

    @Override
    public void buyAsset(BuyRequest request) {

        double marketPrice = getLivePrice(request.getSymbol());
        double cost = marketPrice * request.getQuantity();

        CashBalance cash = getOrCreateCash();
        if (cash.getBalance() < cost) {
            throw new RuntimeException("Insufficient cash balance");
        }

        cash.setBalance(cash.getBalance() - cost);
        cashBalanceRepository.save(cash);

        logTransaction(
                request.getSymbol(),
                request.getAssetType(),
                request.getQuantity(),
                -cost,                 // BUY = negative
                TransactionType.BUY
        );

        PortfolioItem item = portfolioItemRepository
                .findBySymbolAndAssetType(
                        request.getSymbol(),
                        request.getAssetType()
                )
                .orElse(null);

        if (item == null) {
            item = PortfolioItem.builder()
                    .assetType(request.getAssetType())
                    .symbol(request.getSymbol())
                    .quantity(request.getQuantity())
                    .avgBuyPrice(marketPrice)
                    .build();
        } else {
            int totalQty = item.getQuantity() + request.getQuantity();
            double newAvg =
                    ((item.getQuantity() * item.getAvgBuyPrice())
                            + (request.getQuantity() * marketPrice)) / totalQty;

            item.setQuantity(totalQty);
            item.setAvgBuyPrice(newAvg);
        }

        portfolioItemRepository.save(item);
    }

    // ---------- SELL ----------

    @Override
    public void sellAsset(SellRequest request) {

        PortfolioItem item = portfolioItemRepository
                .findBySymbolAndAssetType(
                        request.getSymbol(),
                        request.getAssetType()
                )
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (item.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Not enough quantity to sell");
        }

        double marketPrice = getLivePrice(request.getSymbol());
        double revenue = marketPrice * request.getQuantity();

        CashBalance cash = getOrCreateCash();
        cash.setBalance(cash.getBalance() + revenue);
        cashBalanceRepository.save(cash);
        logTransaction(
                request.getSymbol(),
                request.getAssetType(),
                request.getQuantity(),
                revenue,               // SELL = positive
                TransactionType.SELL
        );

        int remaining = item.getQuantity() - request.getQuantity();
        if (remaining == 0) {
            portfolioItemRepository.delete(item);
        } else {
            item.setQuantity(remaining);
            portfolioItemRepository.save(item);
        }
    }

    // ---------- DASHBOARD ----------

    @Override
    public DashboardResponse getDashboard() {

        List<PortfolioItem> items = portfolioItemRepository.findAll();
        CashBalance cash = getOrCreateCash();

        double totalAssetValue = 0;
        List<DashboardResponse.AssetSummary> summaries = new ArrayList<>();

        for (PortfolioItem item : items) {
            double currentPrice = getLivePrice(item.getSymbol());

            double invested = item.getQuantity() * item.getAvgBuyPrice();
            double current = item.getQuantity() * currentPrice;
            double pnl = current - invested;

            totalAssetValue += current;

            summaries.add(
                    DashboardResponse.AssetSummary.builder()
                            .symbol(item.getSymbol())
                            .assetType(item.getAssetType().name())
                            .quantity(item.getQuantity())
                            .avgBuyPrice(item.getAvgBuyPrice())
                            .currentPrice(currentPrice)
                            .investedValue(invested)
                            .currentValue(current)
                            .profitOrLoss(pnl)
                            .build()
            );
        }

        return DashboardResponse.builder()
                .cashBalance(cash.getBalance())
                .totalAssetValue(totalAssetValue)
                .totalPortfolioValue(cash.getBalance() + totalAssetValue)
                .assets(summaries)
                .build();
    }

    // ---------- YAHOO ----------

//    private double getLivePrice(String symbol) {
//        try {
//            String url =
//                    "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol;
//
//            YahooResponse response =
//                    restTemplate.getForObject(url, YahooResponse.class);
//
//            return response.getChart()
//                    .getResult()
//                    .get(0)
//                    .getMeta()
//                    .getRegularMarketPrice();
//
//        } catch (Exception e) {
//            return 100.0; // fallback for demo safety
//        }
//    }

    private double getLivePrice(String symbol) {
        try {
            String url = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol;

            System.out.println("Calling Yahoo API for symbol: " + symbol);
            System.out.println("URL: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<YahooResponse> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            YahooResponse.class
                    );

            YahooResponse body = response.getBody();

            double price = body
                    .getChart()
                    .getResult()
                    .get(0)
                    .getMeta()
                    .getRegularMarketPrice();

            System.out.println("Yahoo price for " + symbol + " = " + price);

            return price;

        } catch (Exception e) {
            System.out.println("Yahoo API FAILED for " + symbol + ", using fallback");
            return 100.0; // fallback
        }
    }

    private void logTransaction(
            String symbol,
            AssetType assetType,
            int quantity,
            double amount,
            TransactionType type
    ) {
        TransactionLog tx = TransactionLog.builder()
                .transactionId(java.util.UUID.randomUUID().toString())
                .symbol(symbol)
                .assetType(assetType)
                .quantity(quantity)
                .amount(amount)
                .transactionType(type)
                .build();

        transactionLogRepository.save(tx);
    }

    public List<TransactionResponse> getAllTransactions() {

        return transactionLogRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(tx -> TransactionResponse.builder()
                        .transactionId(tx.getTransactionId())
                        .symbol(tx.getSymbol())
                        .assetType(tx.getAssetType().name())
                        .quantity(tx.getQuantity())
                        .amount(tx.getAmount())
                        .transactionType(tx.getTransactionType().name())
                        .createdAt(tx.getCreatedAt())
                        .build()
                )
                .toList();
    }



}
