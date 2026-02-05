import React, { useState, useEffect } from "react";
import axios from "axios";
import { buyAsset } from "../api/portfolioApi";
import { searchYahooSymbols } from "../api/yahooApi";
import "./BuyAsset.css";

function BuyAsset() {
  const [form, setForm] = useState({
    assetType: "STOCK",
    symbol: "",
    quantity: ""
  });

  const [suggestions, setSuggestions] = useState([]);
  const [toast, setToast] = useState({
    message: "",
    type: "" // "success" | "error"
  });

  const [gainers, setGainers] = useState([]);

  /* ================= YAHOO SEARCH (DEBOUNCED) ================= */
  useEffect(() => {
    if (form.symbol.length < 2) {
      setSuggestions([]);
      return;
    }

    const timeout = setTimeout(async () => {
      try {
        const res = await searchYahooSymbols(form.symbol);
        setSuggestions(res.quotes || []);
      } catch (err) {
        setToast({
          message: "Failed to fetch symbol suggestions",
          type: "error"
        });
        setTimeout(
          () => setToast({ message: "", type: "" }),
          3000
        );
      }
    }, 300);

    return () => clearTimeout(timeout);
  }, [form.symbol]);

  /* ================= FETCH TOP GAINERS ================= */
  useEffect(() => {
    const fetchGainers = async () => {
      try {
        const response = await fetch('https://financialmodelingprep.com/stable/biggest-gainers?apikey=ca8avN0dfOOIQMe8i8taDDlTRfxNXAFD');
        const data = await response.json();
        setGainers(data || []);
      } catch (err) {
        console.error('Failed to fetch gainers:', err);
      }
    };

    fetchGainers();
  }, []);

  /* ================= HANDLE BUY ================= */
  const handleBuy = async () => {
    if (!form.symbol || !form.quantity || Number(form.quantity) <= 0) {
      setToast({
        message: "Please enter valid symbol and quantity",
        type: "error"
      });
      setTimeout(
        () => setToast({ message: "", type: "" }),
        3000
      );
      return;
    }

    try {
      await buyAsset({
        assetType: form.assetType,
        symbol: form.symbol,
        quantity: Number(form.quantity)
      });

      setToast({
        message: `Bought ${form.quantity} units of ${form.symbol}`,
        type: "success"
      });

      setTimeout(
        () => setToast({ message: "", type: "" }),
        3000
      );

      // reset form
      setForm({
        assetType: "STOCK",
        symbol: "",
        quantity: ""
      });
      setSuggestions([]);
    } catch (err) {
      setToast({
        message:
          err.response?.data?.message ||
          "Buy failed. Please try again.",
        type: "error"
      });

      setTimeout(
        () => setToast({ message: "", type: "" }),
        3000
      );
    }
  };

  /* ================= RENDER ================= */
  return (
    <div className="buy-asset-container">
      <h2 className="buy-asset-title">Buy Asset</h2>

      <div className="buy-asset-form">
        {/* Asset Type */}
        <select
          value={form.assetType}
          onChange={(e) =>
            setForm({ ...form, assetType: e.target.value })
          }
        >
          <option value="STOCK">STOCK</option>
          <option value="CRYPTO">CRYPTO</option>
        </select>

        {/* Symbol Input + Suggestions */}
        <div className="symbol-input-wrapper">
          <input
            placeholder="Symbol (AAPL, BTC)"
            value={form.symbol}
            onChange={(e) =>
              setForm({
                ...form,
                symbol: e.target.value.toUpperCase()
              })
            }
          />

          {suggestions.length > 0 && (
            <ul className="suggestions-list">
              {suggestions.map((s) => (
                <li
                  key={s.symbol}
                  onClick={() => {
                    setForm({ ...form, symbol: s.symbol });
                    setSuggestions([]);
                  }}
                >
                  <strong>{s.symbol}</strong>
                  <span className="muted">
                    {" "}
                    {s.shortname || s.longname}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </div>

        {/* Quantity */}
        <input
          type="number"
          placeholder="Quantity"
          value={form.quantity}
          onChange={(e) =>
            setForm({ ...form, quantity: e.target.value })
          }
        />

        {/* Buy Button */}
        <button className="buy-asset-button" onClick={handleBuy}>
          Buy
        </button>
      </div>

      {/* ===== Top Gainers ===== */}
      <div className="top-gainers-container">
        <h3>Top Gainers</h3>
        {gainers.length > 0 ? (
          <ul className="gainers-list">
            {gainers.slice(0, 10).map((gainer, index) => (
              <li key={index} className="gainer-item">
                <span className="gainer-symbol">{gainer.symbol}</span>
                <span className="gainer-name">{gainer.name}</span>
                <span className="gainer-change positive">+{gainer.change?.toFixed(2) || gainer.changesPercentage?.toFixed(2) + '%'}</span>
              </li>
            ))}
          </ul>
        ) : (
          <p>Loading top gainers...</p>
        )}
      </div>

      {/* ===== Toast ===== */}
      {toast.message && (
        <div className={`toast ${toast.type}`}>
          {toast.message}
        </div>
      )}
    </div>
  );
}

export default BuyAsset;
