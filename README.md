# 📎 Vite URL Shortener

A scalable URL shortener app with real-time click analytics built using:

- 🔧 Spring Boot microservices
- 📩 Kafka for event-driven communication
- 🐳 Docker for local development
- 🔐 JWT + Google OAuth2 SSO
- 🔎 Swagger for API docs
- ⚛️ React + Redux frontend
- 📚 Storybook for UI component development
- 🛡️ Kong as API gateway (JWT validation)

---

## 📐 Architecture Overview

**Microservices:**

- **Auth Service** – OAuth2 SSO, JWT issuance
- **URL Service** – Shorten URLs, list user's URLs
- **Redirector Service** – Redirects short URLs, publishes click events
- **Analytics Service** – Consumes Kafka events, provides click stats
- **React Frontend** – Login, dashboard, analytics

**Infrastructure:**

- Kafka with KRaft
- PostgreSQL
- Docker Compose (local)
- Terraform files for deploying and managing infrastructure on AWS

> **Note:** Terraform support is provided for AWS deployments.

---

## 🧪 Testing

- Java: JUnit & Mockito
- React: Jest + React Testing Library

---

## 🚀 Getting Started (Local Dev)

### ✅ Prerequisites

- Java 21+
- Node.js 18+
- Docker + Docker Compose

---

### 🛠️ Run All Services Locally

```bash
git clone https://github.com/ganoninc/url-shortener-app.git
cd url-shortener-app
docker-compose up --build
```

---

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.
