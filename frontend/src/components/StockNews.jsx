import React, { useEffect, useMemo, useState } from "react";
import { fetchStockNews } from "../api/newsApi";
import "./StockNews.css";

function formatAlphaVantageDate(timePublished) {
  // Alpha Vantage format: "20240101T120000"
  if (!timePublished || typeof timePublished !== "string") return "";
  const m = timePublished.match(
    /^(\d{4})(\d{2})(\d{2})T(\d{2})(\d{2})(\d{2})$/
  );
  if (!m) return timePublished;
  const [, y, mo, d, hh, mm] = m;
  const iso = `${y}-${mo}-${d}T${hh}:${mm}:00Z`;
  const dt = new Date(iso);
  if (Number.isNaN(dt.getTime())) return timePublished;
  return dt.toLocaleString();
}

/**
 * Stock News (Frontend Only)
 *
 * Displays stock-market related headlines fetched directly from a public API.
 * Env var required (CRA): REACT_APP_NEWS_API_KEY
 */
function StockNews() {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const canFetch = useMemo(
    // ✅ NEWS-RELATED LOGIC: warn when key missing (no hardcoding)
    () => Boolean(process.env.REACT_APP_NEWS_API_KEY),
    []
  );

  const loadNews = async () => {
    setLoading(true);
    setError("");
    try {
      const items = await fetchStockNews({
        topics: "financial_markets,economy_fiscal,technology",
        limit: 20
      });
      setArticles(items);
    } catch (e) {
      setArticles([]);
      setError(e?.message || "Failed to load stock news.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadNews();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div className="stock-news">
      <div className="stock-news-header">
        <div>
          <h2 className="stock-news-title">Stock News</h2>
          <p className="stock-news-subtitle">
            Market headlines fetched directly from a public news endpoint.
          </p>
        </div>

        <button
          className="stock-news-refresh"
          onClick={loadNews}
          disabled={loading}
          title="Refresh news"
        >
          {loading ? "Refreshing..." : "Refresh"}
        </button>
      </div>

      {!canFetch && (
        <div className="stock-news-banner warning">
          ⚠️ REACT_APP_NEWS_API_KEY is not set. Add it to `frontend/.env` and
          restart the dev server to enable news.
        </div>
      )}

      {error && !loading && (
        <div className="stock-news-banner error">⚠️ {error}</div>
      )}

      {loading && (
        <div className="stock-news-state">
          <div className="stock-news-skeleton" />
          <div className="stock-news-skeleton" />
          <div className="stock-news-skeleton" />
        </div>
      )}

      {!loading && !error && articles.length === 0 && (
        <div className="stock-news-state empty">
          No news articles found. Try refreshing in a moment.
        </div>
      )}

      {!loading && articles.length > 0 && (
        <div className="stock-news-grid">
          {articles.map((a) => (
            <a
              key={a.id}
              className="news-card"
              href={a.url}
              target="_blank"
              rel="noreferrer"
              title="Open original article"
            >
              <div className="news-card-top">
                <div className="news-source">{a.source || "Source"}</div>
                <div className="news-date">
                  {formatAlphaVantageDate(a.publishedAt)}
                </div>
              </div>
              <div className="news-title">{a.title}</div>
              <div className="news-desc">{a.description}</div>
              <div className="news-open">
                Open article <span aria-hidden="true">↗</span>
              </div>
            </a>
          ))}
        </div>
      )}
    </div>
  );
}

export default StockNews;

