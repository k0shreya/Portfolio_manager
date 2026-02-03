import React, { useEffect, useState } from "react";
import { getDashboard } from "../api/portfolioApi";
import "./Dashboard.css";

function Dashboard() {
  const [data, setData] = useState(null);

  useEffect(() => {
    fetchDashboard();
  }, []);

  const fetchDashboard = async () => {
    const res = await getDashboard();
    setData(res.data);
  };

  if (!data) return <p className="dashboard-loading">Loading...</p>;

  return (
    <div className="dashboard-container">
      <h2 className="dashboard-title">Portfolio Dashboard</h2>

      {/* Top summary cards */}
      <div className="dashboard-summary">
        <div className="summary-card">
          <p className="summary-label">Cash Balance</p>
          <p className="summary-value">$ {data.cashBalance}</p>
        </div>

        <div className="summary-card">
          <p className="summary-label">Total Asset Value</p>
          <p className="summary-value">$ {data.totalAssetValue}</p>
        </div>

        <div className="summary-card">
          <p className="summary-label">Portfolio Value</p>
          <p className="summary-value">$ {data.totalPortfolioValue}</p>
        </div>
      </div>

      {/* Assets table */}
      <div className="assets-section">
        <h3 className="assets-title">Assets</h3>

        <table className="assets-table">
          <thead>
            <tr>
              <th>Symbol</th>
              <th>Type</th>
              <th>Qty</th>
              <th>Avg Buy</th>
              <th>Current</th>
              <th>P / L</th>
            </tr>
          </thead>
          <tbody>
            {data.assets.map((a, i) => (
              <tr key={i}>
                <td>{a.symbol}</td>
                <td>{a.assetType}</td>
                <td>{a.quantity}</td>
                <td>{a.avgBuyPrice}</td>
                <td>{a.currentPrice}</td>
                <td
                  className={
                    a.profitOrLoss >= 0 ? "profit" : "loss"
                  }
                >
                  {a.profitOrLoss}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default Dashboard;
