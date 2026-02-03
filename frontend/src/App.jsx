import React from "react";
import CashBalance from "./components/CashBalance";
import BuyAsset from "./components/BuyAsset";
import SellAsset from "./components/SellAsset";
import Dashboard from "./components/Dashboard";
import "./App.css";

function App() {
  return (
    <div className="app-container">
      <header className="app-header">Portfolio Manager</header>

      <main className="app-content">
        <div className="top-section">
          <CashBalance />
        </div>

        <div className="actions-section">
          <BuyAsset />
          <SellAsset />
        </div>

        <Dashboard />
      </main>
    </div>
  );
}

export default App;
