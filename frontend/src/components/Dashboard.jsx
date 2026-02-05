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

  // ---- Line: Profit / Loss by symbol
  const lineData = data.assets.map((a) => ({
    symbol: a.symbol,
    pnl: Number(a.profitOrLoss.toFixed(2))
  }));

  
  

  const COLORS = ["#3b82f6", "#22c55e", "#f97316", "#ef4444"];

  /* ================= RENDER ================= */

  return (
    <div className="dashboard-container">
      <h2 className="dashboard-title">Holdings</h2>

      {/* ===== Summary Cards ===== */}
      <div className="dashboard-summary">
        <div className="summary-card">
          <p className="summary-label">Cash Balance</p>
          <p className="summary-value">
            $ {formatAmount(data.cashBalance)}
          </p>
        </div>

        <div className="summary-card">
          <p className="summary-label">Total Asset Value</p>
          <p className="summary-value">
            $ {formatAmount(data.totalAssetValue)}
          </p>
        </div>

        <div className="summary-card">
          <p className="summary-label">Portfolio Value</p>
          <p className="summary-value">
            $ {formatAmount(data.totalPortfolioValue)}
          </p>
        </div>
      </div>

      {/* ===== Charts Section ===== */}
      <div className="dashboard-charts">
        {/* ===== Pie Chart ===== */}
        <div className="chart-card">
          <h4>Asset Allocation</h4>

          <ResponsiveContainer width="100%" height={220}>
            <PieChart>
              <Pie
                data={pieData}
                dataKey="value"
                nameKey="name"
                outerRadius={80}
                label={({ percent }) =>
                  `${(percent * 100).toFixed(1)}%`
                }
              >
                {pieData.map((entry, index) => (
                  <Cell
                    key={entry.name}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>

          {/*  Custom legend */}
          <div className="pie-legend">
            {pieData.map((entry, index) => (
              <div key={entry.name} className="legend-item">
                <span
                  className="legend-color"
                  style={{
                    backgroundColor:
                      COLORS[index % COLORS.length]
                  }}
                />
                <span className="legend-text">
                  {entry.name}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* ===== Bar Chart ===== */}
        <div className="chart-card">
          <h4>Value by Symbol</h4>
          <ResponsiveContainer width="100%" height={260}>
            <BarChart data={barData}>
              <XAxis dataKey="symbol" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="value" fill="#6366f1" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* ===== Line Chart ===== */}
        <div className="chart-card">
          <h4>Profit / Loss</h4>
          <ResponsiveContainer width="100%" height={260}>
            <LineChart data={lineData}>
              <XAxis dataKey="symbol" />
              <YAxis />
              <Tooltip />
              <Line
                type="monotone"
                dataKey="pnl"
                stroke="#16a34a"
                strokeWidth={3}
              />
            </LineChart>
          </ResponsiveContainer>
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
