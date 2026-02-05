package com.example.portfolio.controller;

import com.example.portfolio.beans.AssetType;
import com.example.portfolio.dto.*;
import com.example.portfolio.service.PortfolioItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioItemControllerTest {

    @Mock
    private PortfolioItemService portfolioItemService;

    @InjectMocks
    private PortfolioItemController controller;

    @Test
    void buy_forwards_request_to_service() {
        BuyRequest req = BuyRequest.builder().symbol("AAPL").assetType(AssetType.STOCK).quantity(1).build();
        doNothing().when(portfolioItemService).buyAsset(eq(req));

        controller.buy(req);

        verify(portfolioItemService, times(1)).buyAsset(eq(req));
    }

    @Test
    void sell_forwards_request_to_service() {
        SellRequest req = SellRequest.builder().symbol("TSLA").assetType(AssetType.STOCK).quantity(2).build();
        doNothing().when(portfolioItemService).sellAsset(eq(req));

        controller.sell(req);

        verify(portfolioItemService, times(1)).sellAsset(eq(req));
    }

    @Test
    void dashboard_returns_value_from_service() {
        DashboardResponse expected = mock(DashboardResponse.class);
        when(portfolioItemService.getDashboard()).thenReturn(expected);

        DashboardResponse actual = controller.dashboard();

        Assertions.assertSame(expected, actual);
        verify(portfolioItemService, times(1)).getDashboard();
    }

    @Test
    void getCash_returns_value_from_service() {
        CashResponse expected = mock(CashResponse.class);
        when(portfolioItemService.getCash()).thenReturn(expected);

        CashResponse actual = controller.getCash();

        Assertions.assertSame(expected, actual);
        verify(portfolioItemService, times(1)).getCash();
    }

    @Test
    void updateCash_forwards_request_and_returns_service_response() {
        CashUpdateRequest req = CashUpdateRequest.builder().amount(250.0).build();
        CashResponse expected = mock(CashResponse.class);
        when(portfolioItemService.updateCash(eq(req))).thenReturn(expected);

        CashResponse actual = controller.updateCash(req);

        Assertions.assertSame(expected, actual);
        verify(portfolioItemService, times(1)).updateCash(eq(req));
    }

    @Test
    void getTransactions_returns_list_from_service() {
        TransactionResponse t1 = mock(TransactionResponse.class);
        TransactionResponse t2 = mock(TransactionResponse.class);
        List<TransactionResponse> expected = List.of(t1, t2);
        when(portfolioItemService.getAllTransactions()).thenReturn(expected);

        List<TransactionResponse> actual = controller.getTransactions();

        Assertions.assertEquals(expected, actual);
        verify(portfolioItemService, times(1)).getAllTransactions();
    }

    @Test
    void searchYahoo_returns_external_api_body_for_non_empty_query() {
        String q = "bitcoin";
        String expectedUrl = "https://query1.finance.yahoo.com/v1/finance/search?q=" + q + "&quotesCount=5&newsCount=0";
        String body = "{\"result\":\"ok\"}";

        try (var mocked = Mockito.mockConstruction(RestTemplate.class, (mock, ctx) -> {
            when(mock.exchange(eq(expectedUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(ResponseEntity.ok(body));
        })) {
            ResponseEntity<String> resp = controller.searchYahoo(q);
            Assertions.assertEquals(body, resp.getBody());
        }
    }

    @Test
    void searchYahoo_returns_external_api_body_for_empty_query() {
        String q = "";
        String expectedUrl = "https://query1.finance.yahoo.com/v1/finance/search?q=" + q + "&quotesCount=5&newsCount=0";
        String body = "{\"result\":\"empty\"}";

        try (var mocked = Mockito.mockConstruction(RestTemplate.class, (mock, ctx) -> {
            when(mock.exchange(eq(expectedUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(ResponseEntity.ok(body));
        })) {
            ResponseEntity<String> resp = controller.searchYahoo(q);
            Assertions.assertEquals(body, resp.getBody());
        }
    }
}
