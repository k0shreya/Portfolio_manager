// java
package com.example.portfolio.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class YahooResponseTest {

    @Test
    void chartIsNullByDefault() {
        YahooResponse r = new YahooResponse();
        assertNull(r.getChart());
    }

    @Test
    void resultListCanBeEmpty() {
        YahooResponse.Chart chart = new YahooResponse.Chart();
        chart.setResult(List.of());

        YahooResponse r = new YahooResponse();
        r.setChart(chart);

        assertNotNull(r.getChart());
        assertNotNull(r.getChart().getResult());
        assertEquals(0, r.getChart().getResult().size());
    }

    @Test
    void metaDefaultsRegularMarketPriceToZero() {
        YahooResponse.Meta meta = new YahooResponse.Meta();
        assertEquals(0.0, meta.getRegularMarketPrice());
    }

    @Test
    void multipleResultsIndexedCorrectly() {
        YahooResponse.Meta m1 = new YahooResponse.Meta();
        m1.setRegularMarketPrice(100.1);

        YahooResponse.Meta m2 = new YahooResponse.Meta();
        m2.setRegularMarketPrice(200.2);

        YahooResponse.Result r1 = new YahooResponse.Result();
        r1.setMeta(m1);

        YahooResponse.Result r2 = new YahooResponse.Result();
        r2.setMeta(m2);

        YahooResponse.Chart chart = new YahooResponse.Chart();
        chart.setResult(List.of(r1, r2));

        YahooResponse resp = new YahooResponse();
        resp.setChart(chart);

        assertEquals(100.1, resp.getChart().getResult().get(0).getMeta().getRegularMarketPrice(), 1e-9);
        assertEquals(200.2, resp.getChart().getResult().get(1).getMeta().getRegularMarketPrice(), 1e-9);
    }

    @Test
    void resultListFromListOfIsImmutable() {
        YahooResponse.Meta m = new YahooResponse.Meta();
        m.setRegularMarketPrice(1.23);

        YahooResponse.Result res = new YahooResponse.Result();
        res.setMeta(m);

        YahooResponse.Chart chart = new YahooResponse.Chart();
        chart.setResult(List.of(res));

        YahooResponse resp = new YahooResponse();
        resp.setChart(chart);

        assertThrows(UnsupportedOperationException.class, () -> resp.getChart().getResult().add(new YahooResponse.Result()));
    }

    @Test
    void instancesAreIndependentWhenModified() {
        YahooResponse.Meta mA = new YahooResponse.Meta();
        mA.setRegularMarketPrice(10.0);
        YahooResponse.Result a = new YahooResponse.Result();
        a.setMeta(mA);

        YahooResponse.Meta mB = new YahooResponse.Meta();
        mB.setRegularMarketPrice(20.0);
        YahooResponse.Result b = new YahooResponse.Result();
        b.setMeta(mB);

        // modify a
        a.getMeta().setRegularMarketPrice(99.99);

        assertEquals(99.99, a.getMeta().getRegularMarketPrice(), 1e-9);
        assertEquals(20.0, b.getMeta().getRegularMarketPrice(), 1e-9);
    }

    @Test
    void preservesHighPrecisionRegularMarketPrice() {
        double precise = 12345678.901234567;
        YahooResponse.Meta meta = new YahooResponse.Meta();
        meta.setRegularMarketPrice(precise);

        assertEquals(precise, meta.getRegularMarketPrice(), 1e-12);
    }
}
