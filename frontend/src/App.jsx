import React, { useState, useEffect } from "react";
import CashBalance from "./components/CashBalance";
import BuyAsset from "./components/BuyAsset";
import SellAsset from "./components/SellAsset";
import Dashboard from "./components/Dashboard";
import TransactionLog from "./components/TransactionLog";
import "./App.css";

function App() {
  const [darkMode, setDarkMode] = useState(false);
  const [activeView, setActiveView] = useState("dashboard"); // default

  useEffect(() => {
    document.body.classList.toggle("dark", darkMode);
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
            {darkMode ? "☀ Light" : "🌙 Dark"}
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
      </nav>

      {/* ===== Main Content ===== */}
      <main className="app-content">
        {activeView === "dashboard" && <Dashboard />}
        {activeView === "cash" && <CashBalance />}
        {activeView === "buy" && <BuyAsset />}
        {activeView === "sell" && <SellAsset />}
        {activeView === "transactions" && <TransactionLog />}
      </main>
    </div>
  );
}

export default App;
