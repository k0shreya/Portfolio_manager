import React, { useState } from "react";
import { sellAsset } from "../api/portfolioApi";
import "./SellAsset.css";

function SellAsset() {
  const [form, setForm] = useState({
    assetType: "STOCK",
    symbol: "",
    quantity: ""
  });

  const handleSell = async () => {
    await sellAsset({
      assetType: form.assetType,
      symbol: form.symbol,
      quantity: Number(form.quantity)
    });
    alert("Asset sold successfully");
  };

  return (
    <div className="sell-asset-container">
      <h2 className="sell-asset-title">Sell Asset</h2>

      <div className="sell-asset-form">
        <select
          value={form.assetType}
          onChange={(e) => setForm({ ...form, assetType: e.target.value })}
        >
          <option value="STOCK">STOCK</option>
          <option value="CRYPTO">CRYPTO</option>
        </select>

        <input
          placeholder="Symbol"
          value={form.symbol}
          onChange={(e) => setForm({ ...form, symbol: e.target.value })}
        />

        <input
          type="number"
          placeholder="Quantity"
          value={form.quantity}
          onChange={(e) => setForm({ ...form, quantity: e.target.value })}
        />

        <button className="sell-asset-button" onClick={handleSell}>
          Sell
        </button>
      </div>
    </div>
  );
}

export default SellAsset;
