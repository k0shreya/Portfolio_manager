import React, { useState, useRef, useEffect } from "react";
import { sendMessageToGemini } from "../api/geminiApi";
import "./GeminiChatbot.css";

/**
 * Gemini Chatbot Component
 * 
 * A frontend-only chatbot component that integrates with Google Gemini API.
 * Requires REACT_APP_GEMINI_API_KEY environment variable to be set.
 */
function GeminiChatbot() {
  const [messages, setMessages] = useState([
    {
      role: "assistant",
      text: "Hello! I'm your AI portfolio assistant. How can I help you today?",
      timestamp: new Date()
    }
  ]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const messagesEndRef = useRef(null);
  const inputRef = useRef(null);

  // Auto-scroll to bottom when new messages arrive
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // Focus input on mount
  useEffect(() => {
    inputRef.current?.focus();
  }, []);

  const handleSend = async (e) => {
    e.preventDefault();
    
    if (!input.trim() || loading) {
      return;
    }

    const userMessage = input.trim();
    setInput("");
    setError(null);

    // Add user message to chat
    const newUserMessage = {
      role: "user",
      text: userMessage,
      timestamp: new Date()
    };
    setMessages(prev => [...prev, newUserMessage]);
    setLoading(true);

    try {
      // Build conversation history (last 10 messages for context)
      const conversationHistory = messages
        .slice(-10)
        .map(msg => ({
          role: msg.role === "assistant" ? "model" : "user",
          text: msg.text
        }));

      // Call Gemini API
      const response = await sendMessageToGemini(userMessage, conversationHistory);

      // Add assistant response to chat
      const assistantMessage = {
        role: "assistant",
        text: response,
        timestamp: new Date()
      };
      setMessages(prev => [...prev, assistantMessage]);
    } catch (err) {
      console.error("Chatbot error:", err);
      setError(err.message || "Failed to get response. Please check your API key and try again.");
      
      // Add error message to chat
      const errorMessage = {
        role: "assistant",
        text: `Sorry, I encountered an error: ${err.message || "Unknown error"}. Please make sure REACT_APP_GEMINI_API_KEY is set in your environment variables.`,
        timestamp: new Date(),
        isError: true
      };
      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setLoading(false);
      inputRef.current?.focus();
    }
  };

  const handleClear = () => {
    setMessages([
      {
        role: "assistant",
        text: "Hello! I'm your AI portfolio assistant. How can I help you today?",
        timestamp: new Date()
      }
    ]);
    setError(null);
    inputRef.current?.focus();
  };

  return (
    <div className="chatbot-container">
      <div className="chatbot-header">
        <div className="chatbot-header-content">
          <h3 className="chatbot-title">AI Assistant</h3>
          <p className="chatbot-subtitle">Ask me anything about your portfolio</p>
        </div>
        <button className="chatbot-clear-btn" onClick={handleClear} title="Clear chat">
          🗑️ Clear
        </button>
      </div>

      <div className="chatbot-messages">
        {messages.map((message, index) => (
          <div
            key={index}
            className={`message ${message.role} ${message.isError ? "error" : ""}`}
          >
            <div className="message-avatar">
              {message.role === "user" ? "👤" : "🤖"}
            </div>
            <div className="message-content">
              <div className="message-text">{message.text}</div>
              <div className="message-time">
                {message.timestamp.toLocaleTimeString([], {
                  hour: "2-digit",
                  minute: "2-digit"
                })}
              </div>
            </div>
          </div>
        ))}
        
        {loading && (
          <div className="message assistant loading">
            <div className="message-avatar">🤖</div>
            <div className="message-content">
              <div className="message-text">
                <span className="typing-indicator">
                  <span></span>
                  <span></span>
                  <span></span>
                </span>
              </div>
            </div>
          </div>
        )}
        
        <div ref={messagesEndRef} />
      </div>

      {error && !loading && (
        <div className="chatbot-error-banner">
          ⚠️ {error}
        </div>
      )}

      <form className="chatbot-input-form" onSubmit={handleSend}>
        <input
          ref={inputRef}
          type="text"
          className="chatbot-input"
          placeholder="Type your message here..."
          value={input}
          onChange={(e) => setInput(e.target.value)}
          disabled={loading}
        />
        <button
          type="submit"
          className="chatbot-send-btn"
          disabled={loading || !input.trim()}
        >
          {loading ? "⏳" : "➤"}
        </button>
      </form>

      {!process.env.REACT_APP_GEMINI_API_KEY && (
        <div className="chatbot-api-warning">
          ⚠️ Warning: REACT_APP_GEMINI_API_KEY is not set. Please add it to your .env file.
        </div>
      )}
    </div>
  );
}

export default GeminiChatbot;
