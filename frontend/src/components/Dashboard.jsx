import React, { useEffect, useState } from "react";
import { getDashboard } from "../api/portfolioApi";
import "./Dashboard.css";
import {
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer
} from "recharts";

function Dashboard() {
  const [data, setData] = useState(null);

  useEffect(() => {
    fetchDashboard();
  }, []);

  const fetchDashboard = async () => {
    const res = await getDashboard();
    setData(res.data);
  };

  const formatAmount = (value) => Number(value).toFixed(2);

  if (!data) {
    return <p className="dashboard-loading">Loading...</p>;
  }

  /* ================= DATA TRANSFORMATION ================= */

  // ---- Pie: Asset allocation by type
  const assetTypeMap = {};
  data.assets.forEach((a) => {
    const value = a.quantity * a.currentPrice;
    assetTypeMap[a.assetType] =
      (assetTypeMap[a.assetType] || 0) + value;
  });

  const pieData = Object.keys(assetTypeMap).map((type) => ({
    name: type,
    value: Number(assetTypeMap[type].toFixed(2))
  }));

  // ---- Bar: Value by symbol
  const barData = data.assets.map((a) => ({
    symbol: a.symbol,
    value: Number((a.quantity * a.currentPrice).toFixed(2))
  }));

  // ---- Line: Combined data for revenue chart
  const lineData = data.assets.map((a) => ({
    symbol: a.symbol,
    pnl: Number(a.profitOrLoss.toFixed(2)),
    value: Number((a.quantity * a.currentPrice).toFixed(2))
  }));

  
  

  const COLORS = ["#3b82f6", "#22c55e", "#f97316", "#ef4444"];

  /* ================= RENDER ================= */

  // Calculate total profit/loss
  const totalPnL = data.assets.reduce((sum, a) => sum + a.profitOrLoss, 0);

  return (
    <div className="dashboard-container">
      {/* ===== Summary Statistics Cards ===== */}
      <div className="dashboard-summary">
        <div className="summary-card">
          <div className="summary-card-icon" style={{ backgroundColor: "rgba(34, 197, 94, 0.2)" }}>
            <span style={{ color: "#22c55e" }}>💰</span>
          </div>
          <div className="summary-card-content">
            <p className="summary-label">Cash Balance</p>
            <p className="summary-value">
              ${formatAmount(data.cashBalance)}
            </p>
          </div>
          <div className="summary-indicator" style={{ backgroundColor: "#22c55e" }}></div>
        </div>

        <div className="summary-card">
          <div className="summary-card-icon" style={{ backgroundColor: "rgba(168, 85, 247, 0.2)" }}>
            <span style={{ color: "#a855f7" }}>📊</span>
          </div>
          <div className="summary-card-content">
            <p className="summary-label">Total Asset Value</p>
            <p className="summary-value">
              ${formatAmount(data.totalAssetValue)}
            </p>
          </div>
          <div className="summary-indicator" style={{ backgroundColor: "#a855f7" }}></div>
        </div>

        <div className="summary-card">
          <div className="summary-card-icon" style={{ backgroundColor: "rgba(59, 130, 246, 0.2)" }}>
            <span style={{ color: "#3b82f6" }}>💼</span>
          </div>
          <div className="summary-card-content">
            <p className="summary-label">Portfolio Value</p>
            <p className="summary-value">
              ${formatAmount(data.totalPortfolioValue)}
            </p>
          </div>
          <div className="summary-indicator" style={{ backgroundColor: "#3b82f6" }}></div>
        </div>

        <div className="summary-card">
          <div className="summary-card-icon" style={{ backgroundColor: "rgba(239, 68, 68, 0.2)" }}>
            <span style={{ color: "#ef4444" }}>📈</span>
          </div>
          <div className="summary-card-content">
            <p className="summary-label">Total P / L</p>
            <p className="summary-value">
              ${formatAmount(totalPnL)}
            </p>
          </div>
          <div className="summary-indicator" style={{ backgroundColor: totalPnL >= 0 ? "#22c55e" : "#ef4444" }}></div>
        </div>
      </div>

      {/* ===== Middle Row: Revenue & Transactions ===== */}
      <div className="dashboard-middle-row">
        {/* Revenue Generated Panel */}
        <div className="revenue-panel">
          <h4 className="panel-title">Revenue Generated</h4>
          <p className="revenue-value">${formatAmount(data.totalPortfolioValue)}</p>
          <ResponsiveContainer width="100%" height={200}>
            <LineChart data={lineData}>
              <XAxis dataKey="symbol" stroke="#94a3b8" fontSize={12} />
              <YAxis stroke="#94a3b8" fontSize={12} />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: "#1e293b", 
                  border: "1px solid #334155",
                  borderRadius: "8px"
                }}
              />
              <Line
                type="monotone"
                dataKey="pnl"
                stroke="#a855f7"
                strokeWidth={2}
                dot={false}
              />
              <Line
                type="monotone"
                dataKey="value"
                stroke="#14b8a6"
                strokeWidth={2}
                dot={false}
              />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Recent Transactions Panel */}
        <div className="transactions-panel">
          <h4 className="panel-title">Recent Transactions</h4>
          <div className="transactions-list">
            {data.assets && data.assets.length > 0 ? (
              data.assets.slice(0, 5).map((asset, index) => (
                <div key={index} className="transaction-item">
                  <div className="transaction-info">
                    <span className="transaction-name">{asset.symbol}</span>
                    <span className="transaction-date">
                      {new Date().toLocaleDateString()}
                    </span>
                  </div>
                  <span 
                    className="transaction-badge"
                    style={{ 
                      backgroundColor: index % 2 === 0 ? "rgba(34, 197, 94, 0.2)" : "rgba(20, 184, 166, 0.2)",
                      color: index % 2 === 0 ? "#22c55e" : "#14b8a6"
                    }}
                  >
                    {asset.assetType}
                  </span>
                </div>
              ))
            ) : (
              <div className="transaction-item">
                <div className="transaction-info">
                  <span className="transaction-name">No assets yet</span>
                  <span className="transaction-date">Start by buying assets</span>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* ===== Bottom Row: Additional Insights ===== */}
      <div className="dashboard-insights">
        {/* Campaign / Asset Allocation */}
        <div className="insight-card">
          <h4 className="insight-title">Campaign</h4>
          <ResponsiveContainer width="100%" height={180}>
            <PieChart>
              <Pie
                data={pieData}
                dataKey="value"
                nameKey="name"
                innerRadius={40}
                outerRadius={70}
                label={false}
              >
                {pieData.map((entry, index) => (
                  <Cell
                    key={entry.name}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: "#1e293b", 
                  border: "1px solid #334155",
                  borderRadius: "8px"
                }}
              />
            </PieChart>
          </ResponsiveContainer>
        </div>

        {/* Sales Quantity */}
        <div className="insight-card">
          <h4 className="insight-title">Sales Quantity</h4>
          <ResponsiveContainer width="100%" height={180}>
            <BarChart data={barData}>
              <XAxis dataKey="symbol" stroke="#94a3b8" fontSize={11} />
              <YAxis stroke="#94a3b8" fontSize={11} />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: "#1e293b", 
                  border: "1px solid #334155",
                  borderRadius: "8px"
                }}
              />
              <Bar dataKey="value" fill="#6366f1" radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Geography Based Traffic / Asset Distribution */}
        <div className="insight-card">
          <h4 className="insight-title">Asset Distribution</h4>
          <div className="distribution-list">
            {pieData.map((entry, index) => (
              <div key={entry.name} className="distribution-item">
                <div className="distribution-bar">
                  <div 
                    className="distribution-fill"
                    style={{ 
                      width: `${(entry.value / data.totalAssetValue) * 100}%`,
                      backgroundColor: COLORS[index % COLORS.length]
                    }}
                  ></div>
                </div>
                <div className="distribution-info">
                  <span className="distribution-name">{entry.name}</span>
                  <span className="distribution-value">${formatAmount(entry.value)}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* ===== Assets Table ===== */}
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
                <td>$ {formatAmount(a.avgBuyPrice)}</td>
                <td>$ {formatAmount(a.currentPrice)}</td>
                <td className={a.profitOrLoss >= 0 ? "profit" : "loss"}>
                  $ {formatAmount(a.profitOrLoss)}
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
