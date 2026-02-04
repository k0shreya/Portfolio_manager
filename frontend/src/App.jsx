import React, { useState, useEffect } from "react";
import CashBalance from "./components/CashBalance";
import BuyAsset from "./components/BuyAsset";
import SellAsset from "./components/SellAsset";
import Dashboard from "./components/Dashboard";
import TransactionLog from "./components/TransactionLog";
import GeminiChatbot from "./components/GeminiChatbot";
import "./App.css";

function App() {
  const [activeView, setActiveView] = useState("dashboard");

  // Force dark mode to match the reference image
  useEffect(() => {
    document.body.classList.add("dark");
  }, []);

  return (
    <div className="app-container">
      {/* ===== Sidebar ===== */}
      <aside className="app-sidebar">
        <div className="sidebar-header">
          <h2 className="sidebar-title">Fancy Admin</h2>
        </div>

        <div className="sidebar-profile">
          <div className="profile-avatar">
            <div className="avatar-circle">PM</div>
          </div>
          <div className="profile-info">
            <div className="profile-name">Portfolio Manager</div>
            <div className="profile-role">Administrator</div>
          </div>
        </div>

        <nav className="sidebar-nav">
          <button
            className={`nav-item ${activeView === "dashboard" ? "active" : ""}`}
            onClick={() => setActiveView("dashboard")}
          >
            <span className="nav-icon">📊</span>
            <span className="nav-text">Dashboard</span>
          </button>

          <button
            className={`nav-item ${activeView === "cash" ? "active" : ""}`}
            onClick={() => setActiveView("cash")}
          >
            <span className="nav-icon">💰</span>
            <span className="nav-text">Cash Balance</span>
          </button>

          <button
            className={`nav-item ${activeView === "buy" ? "active" : ""}`}
            onClick={() => setActiveView("buy")}
          >
            <span className="nav-icon">📈</span>
            <span className="nav-text">Buy Asset</span>
          </button>

          <button
            className={`nav-item ${activeView === "sell" ? "active" : ""}`}
            onClick={() => setActiveView("sell")}
          >
            <span className="nav-icon">📉</span>
            <span className="nav-text">Sell Asset</span>
          </button>

          <button
            className={`nav-item ${activeView === "transactions" ? "active" : ""}`}
            onClick={() => setActiveView("transactions")}
          >
            <span className="nav-icon">📋</span>
            <span className="nav-text">Transactions</span>
          </button>

          <button
            className={`nav-item ${activeView === "chatbot" ? "active" : ""}`}
            onClick={() => setActiveView("chatbot")}
          >
            <span className="nav-icon">💬</span>
            <span className="nav-text">AI Assistant</span>
          </button>
        </nav>
      </aside>

      {/* ===== Main Content Area ===== */}
      <main className="app-main">
        <header className="main-header">
          <div className="header-content">
            <h1 className="page-title">
              {activeView === "dashboard" && "DASHBOARD"}
              {activeView === "cash" && "CASH BALANCE"}
              {activeView === "buy" && "BUY ASSET"}
              {activeView === "sell" && "SELL ASSET"}
              {activeView === "transactions" && "TRANSACTIONS"}
              {activeView === "chatbot" && "AI ASSISTANT"}
            </h1>
            <p className="page-subtitle">
              {activeView === "dashboard" && "Welcome to your Dashboard!"}
              {activeView === "cash" && "Manage your cash balance"}
              {activeView === "buy" && "Purchase new assets"}
              {activeView === "sell" && "Sell your holdings"}
              {activeView === "transactions" && "View transaction history"}
              {activeView === "chatbot" && "Ask me anything about your portfolio"}
            </p>
          </div>
          <div className="header-actions">
            <button className="header-icon-btn">🔔</button>
            <button className="header-icon-btn">⚙️</button>
            <button className="header-icon-btn">👤</button>
            <button className="header-btn-primary">Download Reports</button>
          </div>
        </header>

        <div className="main-content">
          {activeView === "dashboard" && <Dashboard />}
          {activeView === "cash" && <CashBalance />}
          {activeView === "buy" && <BuyAsset />}
          {activeView === "sell" && <SellAsset />}
          {activeView === "transactions" && <TransactionLog />}
          {activeView === "chatbot" && <GeminiChatbot />}
        </div>
      </main>
    </div>
  );
}

export default App;
