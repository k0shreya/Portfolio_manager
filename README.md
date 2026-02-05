# Portfolio Manager

A simple portfolio management application with a React frontend and a Spring Boot backend.  
It provides endpoints to manage cash, buy/sell assets, view a dashboard with portfolio summaries and charts, search symbols via a Yahoo-search proxy, and view transaction history.

Repository: https://github.com/k0shreya/Portfolio_manager

---

Table of contents
- [Key features](#key-features)
- [Architecture & tech stack](#architecture--tech-stack)
- [Repository layout](#repository-layout)
- [Prerequisites](#prerequisites)
- [Quick start — run locally](#quick-start---run-locally)
  - [Backend (Spring Boot)](#backend-spring-boot)
  - [Frontend (React)](#frontend-react)
- [API (overview & examples)](#api-overview--examples)
- [Dashboard response shape](#dashboard-response-shape)
- [Tests](#tests)
- [Development notes](#development-notes)
- [Contributing](#contributing)
- [Contact](#contact)

Key features
------------
- View a dashboard summarizing cash, total asset value and portfolio value.
- Asset allocation visualizations in the frontend (pie / bar / line charts).
- Buy and sell assets (backend records transactions and updates holdings).
- Manage cash balance (GET and PUT).
- Search for symbols through a backend Yahoo-search endpoint.
- Transaction history endpoint.
- Basic unit tests for backend DTOs (examples included).

Architecture & tech stack
-------------------------
- Frontend: React (JSX) — located in `frontend/`
  - Calls backend API at `http://localhost:8080/api/portfolio-items` by default.
  - Uses axios and recharts for charts.
- Backend: Java Spring Boot (Maven) — located in `Backend/`
  - REST controllers expose endpoints under `/api/portfolio-items`.
  - Service layer implements business logic (e.g., cash management, buy/sell, dashboard).
  - Uses Lombok for DTOs and builders.
  - A RestTemplate bean is registered for external calls (e.g., Yahoo).
- Build tools:
  - Backend: Maven
  - Frontend: npm

Repository layout
-----------------
- Backend/ (Spring Boot application)
  - src/main/java/com/example/portfolio/...
    - controller/PortfolioItemController.java
    - service/PortfolioItemService(.java + impl)
    - dto/ (DashboardResponse, PortfolioRequest, PortfolioResponse, etc.)
    - PortfolioApplication.java
  - src/test/... (some unit tests for DTOs)
  - pom.xml
- frontend/
  - src/
    - api/portfolioApi.js (axios wrappers)
    - api/yahooApi.js (frontend proxy fetch)
    - components/ (Dashboard.jsx, CashBalance.jsx, SellAsset.jsx, ...)
    - App.jsx
  - package.json

Prerequisites
-------------
- Java 22 and Maven (for backend)
- Node 22 and npm (for frontend)
- Database for persistence (MySQL) . Configure via `application.properties` / environment variables.
- Git

Quick start — run locally
-------------------------

Backend (Spring Boot)
1. Open a terminal in the `Backend/` directory.
2. Build:
   - mvn clean package
3. Run:
   - mvn spring-boot:run
   or run the produced jar:
   - java -jar target/*.jar
4. By default the backend listens on port 8080. The controller enables CORS for `http://localhost:3000` so the React dev server can communicate with it.

Notes about backend configuration:
- If you want persistence, set datasource properties in `src/main/resources/application.properties` (or use environment variables).
- The backend exposes REST endpoints under `/api/portfolio-items` (see API section).
- If you change the backend port, update the frontend API base URL.

Frontend (React)
1. Open a terminal in the `frontend/` directory.
2. Install dependencies:
   - npm install
   (or `yarn install`)
3. Start dev server:
   - npm start
   By default React dev server runs on http://localhost:3000 and expects backend at http://localhost:8080 (see `frontend/src/api/portfolioApi.js` which sets API_BASE_URL to `http://localhost:8080/api/portfolio-items`).

API (overview & examples)
-------------------------
Base path: /api/portfolio-items

- GET /api/portfolio-items/dashboard
  - Returns a DashboardResponse with cash, total values and per-asset summaries.
  - Example: GET http://localhost:8080/api/portfolio-items/dashboard

- GET /api/portfolio-items/cash
  - Returns the current cash balance.
  - Example: GET http://localhost:8080/api/portfolio-items/cash

- PUT /api/portfolio-items/cash
  - Update cash balance.
  - Body example:
    {
      "amount": 5000.00
    }

- POST /api/portfolio-items/buy
  - Register a buy order / increase holdings; expected JSON request matches BuyRequest DTO on backend.
  - Typical payload (frontend wrappers handle fields):
    {
      "assetType": "STOCK",
      "symbol": "AAPL",
      "quantity": 5,
      "price": 150.00
    }
  - Note: exact field names for buy/sell requests are defined in backend DTOs.

- POST /api/portfolio-items/sell
  - Register a sell order / decrease holdings.
  - Payload similar to buy.

- GET /api/portfolio-items/yahoo/search?q={query}
  - Proxy endpoint for searching ticker symbols (frontend calls it when searching symbols).
  - Example: GET http://localhost:8080/api/portfolio-items/yahoo/search?q=apple

- GET /api/portfolio-items/transactions
  - Returns list of recorded transactions (buy/sell).

Dashboard response shape
------------------------
The backend returns a DashboardResponse (see `Backend/src/main/java/com/example/portfolio/dto/DashboardResponse.java`). Key fields:
- cashBalance: double
- totalAssetValue: double
- totalPortfolioValue: double  (cash + assets)
- assets: list of AssetSummary, each with:
  - symbol (String)
  - assetType (String)
  - quantity (int)
  - avgBuyPrice (double)
  - currentPrice (double)
  - investedValue (double)
  - currentValue (double)
  - profitOrLoss (double)

This is what the React dashboard expects and renders into charts and summary cards.

Tests
-----
- Backend tests exist for some DTOs under `Backend/src/test/java/...` and can be run with:
  - mvn test
- Frontend: if tests are added, run:
  - npm test

Development notes
-----------------
- CORS: The backend controller config allows requests from `http://localhost:3000`. If you serve the frontend from a different origin, update CORS settings in `PortfolioItemController`.
- Frontend API base URL: `frontend/src/api/portfolioApi.js` has:
  - const API_BASE_URL = "http://localhost:8080/api/portfolio-items";
  Update this if your backend runs elsewhere.
- Live prices & Yahoo: The backend service contains logic (and commented code) to fetch live prices (it uses RestTemplate). The Yahoo-search proxy endpoint is implemented in the controller; the frontend calls it via `frontend/src/api/yahooApi.js`.
- DTOs use Lombok — ensure your IDE is configured to handle Lombok annotations (install Lombok plugin if needed).

Contributing
------------
Contributions are welcome. Suggested workflow:
1. Fork the repository.
2. Create a feature branch: git checkout -b feature/your-feature
3. Implement changes and add tests where appropriate.
4. Commit and push: git push origin feature/your-feature
5. Open a pull request describing the change.

Please add or update README sections when adding new features or changing run instructions.


Contact
-------
Maintainer: K.Shreya,Shreyas,Siddharth Sharma,Sonali  
Repo: https://github.com/k0shreya/Portfolio_manager

