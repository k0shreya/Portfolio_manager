import React, { useState, useEffect } from "react";
import CashBalance from "./components/CashBalance";
import BuyAsset from "./components/BuyAsset";
import SellAsset from "./components/SellAsset";
import Dashboard from "./components/Dashboard";
import TransactionLog from "./components/TransactionLog";
import News from "./components/News";
import "./App.css";

function App() {
  const [darkMode, setDarkMode] = useState(true);
  const [activeView, setActiveView] = useState("dashboard"); // default

  useEffect(() => {
    // Our default theme variables are dark in :root and light in .light.
    // Add/remove the `light` class when darkMode changes so the toggle works.
    document.body.classList.toggle("light", !darkMode);
  }, [darkMode]);

  return (
    <div className="app-container">
      {/* ===== Header ===== */}
      <header className="app-header">
        <span>Portfolio Manager</span>

        <div style={{ float: "right", display: "flex", gap: "12px" }}>
          <button
            onClick={() => setDarkMode(!darkMode)}
            className="nav-btn"
          >
            {darkMode ? "🌙 Dark" : "☀ Light"}
          </button>
        </div>
      </header>

      {/* ===== Navigation Bar ===== */}
      <nav className="app-nav">
        <button
          className={activeView === "dashboard" ? "active" : ""}
          onClick={() => setActiveView("dashboard")}
        >
          Dashboard
        </button>

        <button
          className={activeView === "cash" ? "active" : ""}
          onClick={() => setActiveView("cash")}
        >
          Cash Balance
        </button>

        <button
          className={activeView === "buy" ? "active" : ""}
          onClick={() => setActiveView("buy")}
        >
          Buy Asset
        </button>

        <button
          className={activeView === "sell" ? "active" : ""}
          onClick={() => setActiveView("sell")}
        >
          Sell Asset
        </button>

        <button
          className={activeView === "transactions" ? "active" : ""}
          onClick={() => setActiveView("transactions")}
        >
          Transactions
        </button>

        <button
          className={activeView === "news" ? "active" : ""}
          onClick={() => setActiveView("news")}
        >
          Stock News
        </button>
      </nav>

      {/* ===== Main Content ===== */}
      <main className="app-content">
        {activeView === "dashboard" && <Dashboard />}
        {activeView === "cash" && <CashBalance />}
        {activeView === "buy" && <BuyAsset />}
        {activeView === "sell" && <SellAsset />}
        {activeView === "transactions" && <TransactionLog />}
        {activeView === "news" && <News />}
      </main>
    </div>
  );
}

export default App;
