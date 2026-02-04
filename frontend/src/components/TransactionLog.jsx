import React, { useEffect, useState } from "react";
import "./TransactionLog.css";

function TransactionLog() {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);

  // Filters
  const [filterType, setFilterType] = useState("ALL");
  const [filterSymbol, setFilterSymbol] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/api/portfolio-items/transactions")
      .then((res) => res.json())
      .then((data) => {
        setTransactions(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching transactions", err);
        setLoading(false);
      });
  }, []);

  // ===== Unique Symbols =====
  const symbols = [...new Set(transactions.map((tx) => tx.symbol))];

  // ===== Combined Filters =====
  const filteredTransactions = transactions.filter((tx) => {
    // Type
    if (filterType !== "ALL" && tx.transactionType !== filterType) {
      return false;
    }

    // Symbol
    if (filterSymbol && tx.symbol !== filterSymbol) {
      return false;
    }

    // Date range
    const txDate = new Date(tx.createdAt);

    if (startDate && txDate < new Date(startDate)) {
      return false;
    }

    if (endDate && txDate > new Date(endDate)) {
      return false;
    }

    return true;
  });

  // ===== Cash Flow Summary (NOT Profit/Loss) =====
  const totalBuy = transactions
    .filter((tx) => tx.transactionType === "BUY")
    .reduce((sum, tx) => sum + Math.abs(tx.amount), 0);

  const totalSell = transactions
    .filter((tx) => tx.transactionType === "SELL")
    .reduce((sum, tx) => sum + tx.amount, 0);

  const amountInvested = totalBuy - totalSell;


  // ===== CSV Export (Filtered Data) =====
  const exportToCSV = () => {
    const headers = [
      "Transaction ID",
      "Date",
      "Symbol",
      "Type",
      "Quantity",
      "Amount",
    ];

    const rows = filteredTransactions.map((tx) => [
      tx.transactionId,
      tx.createdAt,
      tx.symbol,
      tx.transactionType,
      tx.quantity,
      Math.abs(tx.amount),
    ]);

    const csvContent =
      "data:text/csv;charset=utf-8," +
      headers.join(",") +
      "\n" +
      rows.map((row) => row.join(",")).join("\n");

    const encodedUri = encodeURI(csvContent);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", "transaction_log.csv");
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  if (loading) return <p>Loading transactions...</p>;

  return (
    <div className="transaction-container">
      <h3>Transaction Log</h3>

      {/* ===== Summary ===== */}
      <div className="summary-cards">
        <div className="card">
          <h4>Total Invested</h4>
          <p>₹{totalBuy.toFixed(2)}</p>
        </div>

        <div className="card">
          <h4>Total Realized</h4>
          <p>₹{totalSell.toFixed(2)}</p>
        </div>

        <div className="card">
          <h4>Amount Invested</h4>
            <p className="neutral">
             ₹{amountInvested.toFixed(2)}
            </p>

        </div>
      </div>

      {/* ===== Filters + Export ===== */}
      <div className="filter-bar">
        <div className="filters">
          <label>Type</label>
          <select
            value={filterType}
            onChange={(e) => setFilterType(e.target.value)}
          >
            <option value="ALL">All</option>
            <option value="BUY">Buy</option>
            <option value="SELL">Sell</option>
          </select>

          <label>Symbol</label>
          <select
            value={filterSymbol}
            onChange={(e) => setFilterSymbol(e.target.value)}
          >
            <option value="">All</option>
            {symbols.map((sym) => (
              <option key={sym} value={sym}>
                {sym}
              </option>
            ))}
          </select>

          <label>From</label>
          <input
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
          />

          <label>To</label>
          <input
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
          />
        </div>

        <button className="export-btn" onClick={exportToCSV}>
          Export CSV
        </button>
      </div>

      {/* ===== Table ===== */}
      <table className="transaction-table">
        <thead>
          <tr>
            <th>Date</th>
            <th>Symbol</th>
            <th>Type</th>
            <th>Quantity</th>
            <th>Amount</th>
          </tr>
        </thead>

        <tbody>
          {filteredTransactions.map((tx) => (
            <tr key={tx.transactionId}>
              <td>{new Date(tx.createdAt).toLocaleString()}</td>
              <td>{tx.symbol}</td>
              <td className={tx.transactionType === "BUY" ? "buy" : "sell"}>
                {tx.transactionType}
              </td>
              <td>{tx.quantity}</td>
              <td>₹{Math.abs(tx.amount).toFixed(2)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default TransactionLog;
