export const searchYahooSymbols = async (query) => {
  const res = await fetch(
    `http://localhost:8080/api/portfolio-items/yahoo/search?q=${query}`
  );
  return res.json();
};
