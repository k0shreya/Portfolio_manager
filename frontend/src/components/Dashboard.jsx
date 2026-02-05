import React, { useEffect, useState } from "react";
import { getDashboard } from "../api/portfolioApi";
import "./Dashboard.css";
import {
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  AreaChart,
  Area,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
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
                outerRadius={70}
                innerRadius={35}
                label={({ percent }) =>
                  `${(percent * 100).toFixed(0)}%`
                }
              >
                {pieData.map((entry, index) => (
                  <Cell
                    key={entry.name}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>
            </PieChart>
          </ResponsiveContainer>

          {/* ✅ Custom legend (inside card, not SVG) */}
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
            <BarChart data={barData} margin={{ top: 10, right: 20, left: 0, bottom: 0 }}>
              <CartesianGrid strokeDasharray="3 3" stroke="rgba(100, 116, 139, 0.15)" />
              <XAxis dataKey="symbol" stroke="rgba(100, 116, 139, 0.5)" />
              <YAxis stroke="rgba(100, 116, 139, 0.5)" />
              <Tooltip 
                contentStyle={{
                  backgroundColor: "rgba(15, 20, 30, 0.95)",
                  border: "1px solid rgba(59, 130, 246, 0.3)",
                  borderRadius: "8px",
                  color: "#e5e7eb"
                }}
              />
              <Bar dataKey="value" fill="#3b82f6" radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* ===== Line Chart ===== */}
        <div className="chart-card">
          <h4>Profit / Loss</h4>
          <ResponsiveContainer width="100%" height={260}>
            <AreaChart data={lineData} margin={{ top: 10, right: 20, left: 0, bottom: 0 }}>
              <defs>
                <linearGradient id="colorPnl" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#22c55e" stopOpacity={0.3} />
                  <stop offset="95%" stopColor="#22c55e" stopOpacity={0} />
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="rgba(100, 116, 139, 0.15)" />
              <XAxis dataKey="symbol" stroke="rgba(100, 116, 139, 0.5)" />
              <YAxis stroke="rgba(100, 116, 139, 0.5)" />
              <Tooltip 
                contentStyle={{
                  backgroundColor: "rgba(15, 20, 30, 0.95)",
                  border: "1px solid rgba(59, 130, 246, 0.3)",
                  borderRadius: "8px",
                  color: "#e5e7eb"
                }}
              />
              <Area
                type="monotone"
                dataKey="pnl"
                stroke="#22c55e"
                strokeWidth={3}
                fill="url(#colorPnl)"
              />
            </AreaChart>
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
