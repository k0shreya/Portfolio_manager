import React, { useEffect, useState } from "react";
import { getCashBalance, updateCashBalance } from "../api/portfolioApi";
import "./CashBalance.css";

function CashBalance() {
  const [cash, setCash] = useState(0);
  const [amount, setAmount] = useState("");

  useEffect(() => {
    fetchCash();
  }, []);

  const fetchCash = async () => {
    const res = await getCashBalance();
    setCash(res.data.cashBalance);
  };

  // ✅ helper function (closed properly)
  const formatAmount = (value) => {
    return Number(value).toFixed(2);
  };

  const addCash = async () => {
    await updateCashBalance(Number(amount));
    setAmount("");
    fetchCash();
  };

  return (
    <div className="cash-balance-container">
      <h2 className="cash-balance-title">Cash Balance</h2>

      <div className="cash-balance-amount">$ {formatAmount(cash)}</div>

      <div className="cash-balance-form">
        <input
          type="number"
          placeholder="Add amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />
        <button className="cash-balance-button" onClick={addCash}>
          Add Cash
        </button>
      </div>
    </div>
  );
}

export default CashBalance;
