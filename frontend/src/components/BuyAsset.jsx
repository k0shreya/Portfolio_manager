import React, { useState } from "react";
import { buyAsset } from "../api/portfolioApi";
import "./BuyAsset.css";

function BuyAsset() {
  const [form, setForm] = useState({
    assetType: "STOCK",
    symbol: "",
    quantity: ""
  });

  const handleBuy = async () => {
    await buyAsset({
      assetType: form.assetType,
      symbol: form.symbol,
      quantity: Number(form.quantity)
    });
    alert("Asset bought successfully");
  };

  return (
    <div className="buy-asset-container">
      <h2 className="buy-asset-title">Buy Asset</h2>

      <div className="buy-asset-form">
        <select
          value={form.assetType}
          onChange={(e) => setForm({ ...form, assetType: e.target.value })}
        >
          <option value="STOCK">STOCK</option>
          <option value="CRYPTO">CRYPTO</option>
        </select>

        <input
          placeholder="Symbol (AAPL, BTC)"
          value={form.symbol}
          onChange={(e) => setForm({ ...form, symbol: e.target.value })}
        />

        <input
          type="number"
          placeholder="Quantity"
          value={form.quantity}
          onChange={(e) => setForm({ ...form, quantity: e.target.value })}
        />

        <button className="buy-asset-button" onClick={handleBuy}>
          Buy
        </button>
      </div>
    </div>
  );
}

export default BuyAsset;
