import React, { useState, useEffect } from "react";
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
  const [toast, setToast] = useState({ message: "", type: "" });
  const [gainers, setGainers] = useState([]);

  /* ================= YAHOO SEARCH ================= */
  useEffect(() => {
    if (form.symbol.length < 2) {
      setSuggestions([]);
      return;
    }

    const timeout = setTimeout(async () => {
      try {
        const res = await searchYahooSymbols(form.symbol);
        setSuggestions(res.quotes || []);
      } catch {
        showToast("Failed to fetch symbol suggestions", "error");
      }
    }, 300);

    return () => clearTimeout(timeout);
  }, [form.symbol]);

  /* ================= FETCH TOP GAINERS ================= */
  useEffect(() => {
    const fetchGainers = async () => {
      try {
        const res = await fetch(
          "https://financialmodelingprep.com/stable/biggest-gainers?apikey=ca8avN0dfOOIQMe8i8taDDlTRfxNXAFD"
        );
        setGainers(await res.json());
      } catch (err) {
        console.error("Failed to fetch gainers", err);
      }
    };

    fetchGainers();
  }, []);

  /* ================= TOAST HELPER ================= */
  const showToast = (message, type) => {
    setToast({ message, type });
    setTimeout(() => setToast({ message: "", type: "" }), 3000);
  };

  /* ================= HANDLE BUY ================= */
  const handleBuy = async () => {
    if (!form.symbol || Number(form.quantity) <= 0) {
      showToast("Please enter valid symbol and quantity", "error");
      return;
    }

    try {
      await buyAsset({
        assetType: form.assetType,
        symbol: form.symbol,
        quantity: Number(form.quantity)
      });

      showToast(
        `Bought ${form.quantity} units of ${form.symbol}`,
        "success"
      );

      setForm({ assetType: "STOCK", symbol: "", quantity: "" });
      setSuggestions([]);
    } catch (err) {
      const data = err.response?.data;

      const msg =
        data?.message ||
        data?.error ||
        (typeof data === "string" ? data : null) ||
        "Insufficient cash balance";

      showToast(msg, "error");
    }
  };

  /* ================= RENDER ================= */
  return (
    <>
      <div className="buy-asset-container">
        <h2 className="buy-asset-title">Buy Asset</h2>

        <div className="buy-asset-form">
          <select
            value={form.assetType}
            onChange={(e) =>
              setForm({ ...form, assetType: e.target.value })
            }
          >
            <option value="STOCK">STOCK</option>
            <option value="CRYPTO">CRYPTO</option>
          </select>

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

          <input
            type="number"
            placeholder="Quantity"
            value={form.quantity}
            onChange={(e) =>
              setForm({ ...form, quantity: e.target.value })
            }
          />

          <button className="buy-asset-button" onClick={handleBuy}>
            Buy
          </button>
        </div>

        <div className="top-gainers-container">
          <h3>Top Gainers</h3>
          {gainers.length > 0 ? (
            <ul className="gainers-list">
              {gainers.slice(0, 10).map((g, i) => (
                <li key={i} className="gainer-item">
                  <span className="gainer-symbol">{g.symbol}</span>
                  <span className="gainer-name">{g.name}</span>
                  <span className="gainer-change positive">
                    +{g.change?.toFixed(2)}
                  </span>
                </li>
              ))}
            </ul>
          ) : (
            <p>Loading top gainers...</p>
          )}
        </div>
      </div>

      {/* ✅ TOAST OUTSIDE CONTAINER */}
      {toast.message && (
        <div className={`toast ${toast.type}`}>
          {toast.message}
        </div>
      )}
    </>
  );
}

export default BuyAsset;
