/**
 * Gemini API Integration (Frontend Only)
 * 
 * This file handles communication with Google Gemini API.
 * The API key should be set in environment variable: REACT_APP_GEMINI_API_KEY
 */

const GEMINI_API_URL = 'https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent';

/**
 * Send a message to Gemini API and get a response
 * @param {string} message - The user's message
 * @param {Array} conversationHistory - Previous messages for context (optional)
 * @returns {Promise<string>} - The AI's response
 */
export const sendMessageToGemini = async (message, conversationHistory = []) => {
  const apiKey = process.env.REACT_APP_GEMINI_API_KEY;

  if (!apiKey) {
    throw new Error('Gemini API key not found. Please set REACT_APP_GEMINI_API_KEY in your .env file');
  }

  // Build the conversation context
  const contents = [];
  
  // Add system context for portfolio management
  contents.push({
    role: 'user',
    parts: [{ text: 'You are a helpful AI assistant for a portfolio management application. Help users with questions about their portfolio, investments, and financial planning.' }]
  });
  contents.push({
    role: 'model',
    parts: [{ text: 'I understand. I\'m here to help with portfolio management questions.' }]
  });

  // Add conversation history
  conversationHistory.forEach(msg => {
    contents.push({
      role: msg.role === 'user' ? 'user' : 'model',
      parts: [{ text: msg.text }]
    });
  });

  // Add current message
  contents.push({
    role: 'user',
    parts: [{ text: message }]
  });

  try {
    const response = await fetch(`${GEMINI_API_URL}?key=${apiKey}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        contents: contents,
        generationConfig: {
          temperature: 0.7,
          topK: 40,
          topP: 0.95,
          maxOutputTokens: 1024,
        },
      }),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.error?.message || `API error: ${response.status} ${response.statusText}`);
    }

    const data = await response.json();
    
    if (data.candidates && data.candidates[0] && data.candidates[0].content) {
      return data.candidates[0].content.parts[0].text;
    } else {
      throw new Error('Unexpected response format from Gemini API');
    }
  } catch (error) {
    console.error('Gemini API Error:', error);
    throw error;
  }
};
