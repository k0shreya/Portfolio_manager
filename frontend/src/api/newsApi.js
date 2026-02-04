/**
 * Stock News API Integration (Frontend Only)
 *
 * Uses Alpha Vantage "NEWS_SENTIMENT" endpoint.
 * - No backend proxying (per requirements)
 * - Uses env var for key (CRA): REACT_APP_NEWS_API_KEY
 *
 * Docs: https://www.alphavantage.co/documentation/#news-sentiment
 */

const BASE_URL = "https://www.alphavantage.co/query";

/**
 * Fetch stock market news.
 * @param {Object} params
 * @param {string} [params.topics] - Comma-separated topics. Example: "financial_markets,technology"
 * @param {number} [params.limit] - Max number of articles to return (client-side capped)
 */
export async function fetchStockNews({ topics = "financial_markets", limit = 20 } = {}) {
  // ✅ NEWS-RELATED LOGIC: Environment-variable API key (do not hardcode)
  const apiKey = process.env.REACT_APP_NEWS_API_KEY;
  if (!apiKey) {
    throw new Error(
      "News API key not found. Please set REACT_APP_NEWS_API_KEY in your frontend environment."
    );
  }

  const url = new URL(BASE_URL);
  url.searchParams.set("function", "NEWS_SENTIMENT");
  url.searchParams.set("topics", topics);
  url.searchParams.set("apikey", apiKey);

  const res = await fetch(url.toString());
  if (!res.ok) {
    throw new Error(`News API error: ${res.status} ${res.statusText}`);
  }

  const data = await res.json();

  // Alpha Vantage sometimes returns { Information: "...rate limit..." }
  if (data?.Information) {
    throw new Error(data.Information);
  }
  if (data?.Note) {
    throw new Error(data.Note);
  }
  if (!data?.feed || !Array.isArray(data.feed)) {
    return [];
  }

  return data.feed.slice(0, limit).map((item) => ({
    id: item.url || item.title,
    title: item.title,
    source: item.source,
    publishedAt: item.time_published, // yyyymmddThhmmss
    description: item.summary,
    url: item.url
  }));
}

