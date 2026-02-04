package com.example.portfolio.service;

import com.example.portfolio.dto.*;

import java.util.List;

public interface PortfolioItemService {

    void buyAsset(BuyRequest request);

    void sellAsset(SellRequest request);

    DashboardResponse getDashboard();

    CashResponse getCash();

    CashResponse updateCash(CashUpdateRequest request);

    List<TransactionResponse> getAllTransactions();
}
