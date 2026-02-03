import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/portfolio-items";

export const getCashBalance = () =>
  axios.get(`${API_BASE_URL}/cash`);

export const updateCashBalance = (amount) =>
  axios.put(`${API_BASE_URL}/cash`, { amount });

export const buyAsset = (data) =>
  axios.post(`${API_BASE_URL}/buy`, data);

export const sellAsset = (data) =>
  axios.post(`${API_BASE_URL}/sell`, data);

export const getDashboard = () =>
  axios.get(`${API_BASE_URL}/dashboard`);
