import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import "./News.css";

const News = () => {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Memoize the filterNews function to avoid unnecessary re-creations
  const filterNews = useCallback((newsData) => {
    // Move IMPORTANT_KEYWORDS array inside useCallback
    const IMPORTANT_KEYWORDS = [
      "stock", "stocks", "shares", "market", "earnings", "profit", "loss", "revenue",
      "acquire", "acquisition", "merger", "ipo", "bond", "fell", "rose", "gain", "drop",
      "surge", "plunge", "results"
    ];

    const isStockRelevant = (title, description) => {
      const text = (title + " " + description).toLowerCase();
      for (let keyword of IMPORTANT_KEYWORDS) {
        if (text.includes(keyword)) {
          return true;
        }
      }
      return false;
    };

    return newsData.filter((article) => {
      const { title, description } = article;
      return isStockRelevant(title, description);
    });
  }, []);  // No need to add IMPORTANT_KEYWORDS as a dependency

  useEffect(() => {
    const fetchStockNews = async () => {
      const API_KEY = process.env.REACT_APP_NEWS_API_KEY;      
    
      const pageSize = 50;
      const url = `https://newsapi.org/v2/top-headlines?category=business&language=en&pageSize=${pageSize}&apiKey=${API_KEY}`;

      try {
        const response = await axios.get(url);
        const newsData = response.data.articles;
        const filteredNews = filterNews(newsData);  // Now, using the memoized function
        setArticles(filteredNews);
      } catch (err) {
        setError("Error fetching data.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchStockNews();
  }, [filterNews]);  // Now it's safe to keep filterNews in the dependency array

  return (
    <div className="news-container">
      <h2 className="news-title">Stock Market News</h2>
      {loading ? (
        <p className="loading">Loading latest stock news...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : articles.length === 0 ? (
        <p className="no-news">No relevant stock news found.</p>
      ) : (
        <div className="news-grid">
          {articles.map((article, index) => (
            <div key={index} className="news-card">
              <a href={article.url} target="_blank" rel="noopener noreferrer" className="news-link">
                <h3 className="news-headline">{article.title}</h3>
              </a>
              <p className="news-description">{article.description}</p>
              <div className="news-meta">
                <span className="news-source">{article.source.name}</span>
                <span className="news-date">{new Date(article.publishedAt).toLocaleDateString()}</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default News;
