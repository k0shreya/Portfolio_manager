import React, { useEffect, useState } from "react";
import { sellAsset, getDashboard } from "../api/portfolioApi";
import "./SellAsset.css";

function SellAsset() {
  const [ownedAssets, setOwnedAssets] = useState([]);
  const [sellQty, setSellQty] = useState({});
  const [error, setError] = useState("");
  const [toast, setToast] = useState({
    message: "",
    type: "" // "success" | "error"
  });

  /* ================= LOAD OWNED ASSETS ================= */
  useEffect(() => {
    fetchOwnedAssets();
  }, []);

  const fetchOwnedAssets = async () => {
    try {
      const res = await getDashboard();
      setOwnedAssets(res.data.assets || []);
    } catch (err) {
      console.error("Failed to load holdings", err);
    }
  };

  /* ================= HANDLE SELL ================= */
  const handleSell = async (asset) => {
    setError("");

    const qty = Number(sellQty[asset.symbol]);

    if (!qty || qty <= 0) {
      setError("Enter a valid quantity to sell.");
      return;
    }

    if (qty > asset.quantity) {
      setError(
        `You only own ${asset.quantity} units of ${asset.symbol}.`
      );
      return;
    }

    try {
      await sellAsset({
        assetType: asset.assetType,
        symbol: asset.symbol,
        quantity: qty
      });

      // ✅ success toast
      setToast({
        message: `Sold ${qty} units of ${asset.symbol}`,
        type: "success"
      });

      // auto-hide toast
      setTimeout(
        () => setToast({ message: "", type: "" }),
        3000
      );

      // reset qty & refresh holdings
      setSellQty((prev) => ({ ...prev, [asset.symbol]: "" }));
      fetchOwnedAssets();
    } catch (err) {
      const msg =
        err.response?.data?.message ||
        "Sell failed. Please try again.";

      setToast({
        message: msg,
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
    <div className="sell-asset-container">
      <h2 className="sell-asset-title">Sell Assets</h2>

      {ownedAssets.length === 0 ? (
        <p>You do not own any assets.</p>
      ) : (
        <div className="sell-table">
          <div className="sell-header">
            <span>Symbol</span>
            <span>Type</span>
            <span>Owned</span>
            <span>Sell Qty</span>
            <span>Action</span>
          </div>

          {ownedAssets.map((asset) => (
            <div className="sell-row" key={asset.symbol}>
              <span>{asset.symbol}</span>
              <span>{asset.assetType}</span>
              <span>{asset.quantity}</span>

              <input
                type="number"
                min="1"
                max={asset.quantity}
                placeholder="Qty"
                value={sellQty[asset.symbol] || ""}
                onChange={(e) =>
                  setSellQty({
                    ...sellQty,
                    [asset.symbol]: e.target.value
                  })
                }
              />

              <button
                className="sell-asset-button"
                onClick={() => handleSell(asset)}
              >
                Sell
              </button>
            </div>
          ))}
        </div>
      )}

      {error && <p className="error-text">{error}</p>}

      {/* ===== Toast ===== */}
      {toast.message && (
        <div className={`toast ${toast.type}`}>
          {toast.message}
        </div>
      )}
    </div>
  );
}

export default SellAsset;
