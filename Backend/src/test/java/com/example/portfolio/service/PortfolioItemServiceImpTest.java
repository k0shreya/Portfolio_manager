package com.example.portfolio.service;

import com.example.portfolio.beans.*;
import com.example.portfolio.dto.*;
import com.example.portfolio.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioItemServiceImpTest {

    @Mock
    private PortfolioItemRepository portfolioItemRepository;

    @Mock
    private CashBalanceRepository cashBalanceRepository;

    @Mock
    private TransactionLogRepository transactionLogRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PortfolioItemServiceImp service;

    private YahooResponse buildYahooResponseWithPrice(double price) {
        YahooResponse resp = new YahooResponse();
        YahooResponse.Chart chart = new YahooResponse.Chart();
        YahooResponse.Result result = new YahooResponse.Result();
        YahooResponse.Meta meta = new YahooResponse.Meta();
        meta.setRegularMarketPrice(price);
        result.setMeta(meta);
        chart.setResult(List.of(result));
        resp.setChart(chart);
        return resp;
    }

    @BeforeEach
    void setup() {
        // default: ensure cash entry exists with id 1 when not overridden
        Mockito.lenient().when(cashBalanceRepository.findById(1))
                .thenReturn(Optional.of(CashBalance.builder().id(1).balance(1000.0).build()));
    }

    @Test
    void getCash_returns_existing_cash_balance() {
        CashBalance cb = CashBalance.builder().id(1).balance(500.0).build();
        when(cashBalanceRepository.findById(1)).thenReturn(Optional.of(cb));

        CashResponse resp = service.getCash();

        Assertions.assertEquals(500.0, resp.getCashBalance());
    }

    @Test
    void updateCash_increases_and_persists_balance() {
        CashBalance cb = CashBalance.builder().id(1).balance(200.0).build();
        when(cashBalanceRepository.findById(1)).thenReturn(Optional.of(cb));
        when(cashBalanceRepository.save(any(CashBalance.class))).thenAnswer(i -> i.getArgument(0));

        CashUpdateRequest req = CashUpdateRequest.builder().amount(150.0).build();
        CashResponse resp = service.updateCash(req);

        Assertions.assertEquals(350.0, resp.getCashBalance());
        verify(cashBalanceRepository).save(argThat(saved -> saved.getBalance() == 350.0));
    }

    @Test
    void buyAsset_creates_portfolio_item_and_debits_cash_when_sufficient_balance() {
        // existing cash 1000 from setup
        String symbol = "AAPL";
        int qty = 2;
        double price = 100.0;
        String url = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol;

        YahooResponse yahooResp = buildYahooResponseWithPrice(price);
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(YahooResponse.class)))
                .thenReturn(ResponseEntity.ok(yahooResp));

        when(portfolioItemRepository.findBySymbolAndAssetType(eq(symbol), any()))
                .thenReturn(Optional.empty());
        when(portfolioItemRepository.save(any(PortfolioItem.class))).thenAnswer(i -> i.getArgument(0));
        when(cashBalanceRepository.save(any(CashBalance.class))).thenAnswer(i -> i.getArgument(0));

        BuyRequest req = BuyRequest.builder()
                .symbol(symbol)
                .assetType(AssetType.STOCK)
                .quantity(qty)
                .build();

        service.buyAsset(req);

        // verify cash debited by price*qty = 200
        verify(cashBalanceRepository).save(argThat(cb -> Math.abs(cb.getBalance() - (1000.0 - price * qty)) < 1e-6));

        // verify portfolio item created with correct fields
        verify(portfolioItemRepository).save(argThat(item ->
                item.getSymbol().equals(symbol)
                        && item.getQuantity() == qty
                        && Math.abs(item.getAvgBuyPrice() - price) < 1e-6
                        && item.getAssetType() == AssetType.STOCK
        ));

        // verify transaction logged
        verify(transactionLogRepository).save(any(TransactionLog.class));
    }

    @Test
    void buyAsset_throws_when_insufficient_cash() {
        CashBalance low = CashBalance.builder().id(1).balance(50.0).build();
        when(cashBalanceRepository.findById(1)).thenReturn(Optional.of(low));

        String symbol = "EXP";
        YahooResponse yahooResp = buildYahooResponseWithPrice(100.0);
        String url = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol;
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(YahooResponse.class)))
                .thenReturn(ResponseEntity.ok(yahooResp));

        BuyRequest req = BuyRequest.builder()
                .symbol(symbol)
                .assetType(AssetType.STOCK)
                .quantity(1)
                .build();

        Assertions.assertThrows(RuntimeException.class, () -> service.buyAsset(req));
        verify(portfolioItemRepository, never()).save(any());
    }

    @Test
    void sellAsset_removes_item_and_credits_cash_when_selling_all_quantity() {
        String symbol = "TSLA";
        PortfolioItem existing = PortfolioItem.builder()
                .symbol(symbol)
                .assetType(AssetType.STOCK)
                .quantity(5)
                .avgBuyPrice(50.0)
                .build();

        when(portfolioItemRepository.findBySymbolAndAssetType(eq(symbol), eq(AssetType.STOCK)))
                .thenReturn(Optional.of(existing));

        double price = 60.0;
        String url = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol;
        YahooResponse yahooResp = buildYahooResponseWithPrice(price);
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(YahooResponse.class)))
                .thenReturn(ResponseEntity.ok(yahooResp));

        when(cashBalanceRepository.findById(1))
                .thenReturn(Optional.of(CashBalance.builder().id(1).balance(100.0).build()));

        SellRequest req = SellRequest.builder()
                .symbol(symbol)
                .assetType(AssetType.STOCK)
                .quantity(5)
                .build();

        service.sellAsset(req);

        // revenue = 60 * 5 = 300; new cash should be 400
        verify(cashBalanceRepository).save(argThat(cb -> Math.abs(cb.getBalance() - 400.0) < 1e-6));
        verify(portfolioItemRepository).delete(existing);
        verify(transactionLogRepository).save(any(TransactionLog.class));
    }

    @Test
    void sellAsset_throws_when_selling_more_than_owned() {
        String symbol = "NFLX";
        PortfolioItem existing = PortfolioItem.builder()
                .symbol(symbol)
                .assetType(AssetType.STOCK)
                .quantity(2)
                .avgBuyPrice(10.0)
                .build();

        when(portfolioItemRepository.findBySymbolAndAssetType(eq(symbol), eq(AssetType.STOCK)))
                .thenReturn(Optional.of(existing));

        SellRequest req = SellRequest.builder()
                .symbol(symbol)
                .assetType(AssetType.STOCK)
                .quantity(5)
                .build();

        Assertions.assertThrows(RuntimeException.class, () -> service.sellAsset(req));
        verify(cashBalanceRepository, never()).save(any());
    }

    @Test
    void getDashboard_returns_aggregated_values_for_assets_and_cash() {
        PortfolioItem item1 = PortfolioItem.builder()
                .symbol("AAA")
                .assetType(AssetType.STOCK)
                .quantity(3)
                .avgBuyPrice(10.0)
                .build();

        PortfolioItem item2 = PortfolioItem.builder()
                .symbol("BBB")
                .assetType(AssetType.CRYPTO)
                .quantity(2)
                .avgBuyPrice(20.0)
                .build();

        when(portfolioItemRepository.findAll()).thenReturn(List.of(item1, item2));
        when(cashBalanceRepository.findById(1)).thenReturn(Optional.of(CashBalance.builder().id(1).balance(50.0).build()));

        when(restTemplate.exchange(eq("https://query1.finance.yahoo.com/v8/finance/chart/AAA"), eq(HttpMethod.GET), any(HttpEntity.class), eq(YahooResponse.class)))
                .thenReturn(ResponseEntity.ok(buildYahooResponseWithPrice(12.0)));
        when(restTemplate.exchange(eq("https://query1.finance.yahoo.com/v8/finance/chart/BBB"), eq(HttpMethod.GET), any(HttpEntity.class), eq(YahooResponse.class)))
                .thenReturn(ResponseEntity.ok(buildYahooResponseWithPrice(25.0)));

        DashboardResponse resp = service.getDashboard();

        Assertions.assertEquals(50.0, resp.getCashBalance());
        Assertions.assertEquals(86.0, resp.getTotalAssetValue());
        Assertions.assertEquals(136.0, resp.getTotalPortfolioValue());

        Map<String, DashboardResponse.AssetSummary> summaryMap = resp.getAssets().stream()
                .collect(HashMap::new, (m, s) -> m.put(s.getSymbol(), s), HashMap::putAll);

        DashboardResponse.AssetSummary sA = summaryMap.get("AAA");
        Assertions.assertNotNull(sA);
        Assertions.assertEquals(3, sA.getQuantity());
        Assertions.assertEquals(12.0, sA.getCurrentPrice());

        DashboardResponse.AssetSummary sB = summaryMap.get("BBB");
        Assertions.assertNotNull(sB);
        Assertions.assertEquals(2, sB.getQuantity());
        Assertions.assertEquals(25.0, sB.getCurrentPrice());
    }
}
