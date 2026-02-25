# SocialHub (Enterprise-Grade Social Media API)

A modern, highly scalable, and secure social media application built with Spring Boot 3 (Backend) and React (Frontend).

## 🚀 Key Features

* **User Authentication**: Secure JWT-based stateless authentication with role-based access control.
* **Core Social Features**: Posts, Comments, Likes, Shares, Private/Public Profiles, and a robust Follow system.
* **Real-time Engine**: WebSocket-based real-time chat and instant notifications.
* **Media Management**: Seamless integration with Supabase Storage for fast content delivery.

## 🛡️ Security & Reliability (New in v2.0)

We've recently upgraded the backend architecture to support production-level traffic and enterprise-grade observability:

* **Distributed Rate Limiting**: Zero-dependency sliding-window counter protecting endpoints against DDoS and brute-force attacks (`60 req/min` per IP).
* **Circuit Breaker Pattern**: Resilience4j ensures graceful degradation of downstream services (Chat, Notifications, Email, Media) preventing cascading failures.
* **API Telemetry & Interceptors**: Custom interceptors track every API call, recording user, method, endpoint, duration, and status.
* **Database Logging & Auto-cleanup**: Comprehensive JPA entity indexing for lightning-fast API usage queries, paired with a cron-based cleanup service to prevent database bloat.
* **Prometheus & Actuator Integration**: Full JVM, memory, and custom metrics (latency, error rates, rate limit breaches) exported to Prometheus via Spring Boot Actuator.
* **Hardened Exception Handling**: Secure, standardized JSON error responses that never leak internal stack traces to the public.

## ⚙️ Tech Stack

### Backend
* **Java 17 & Spring Boot 3.2**
* **Spring Security & JWT**
* **Spring Data JPA & Hibernate**
* **Spring WebSocket**
* **Resilience4j** (Circuit Breaker)
* **Micrometer & Prometheus** (Observability)
* **PostgreSQL** (Primary Datastore)

### Frontend
* **React 18 & Vite**
* **Tailwind CSS & Framer Motion** (Dark Mode UI)

## 🛠️ Setup & Local Development

### Prerequisites
* Java 17+
* Node.js 18+
* PostgreSQL Database
* Maven

### Environment Variables
For optimal security, credentials are never stored in the codebase. Create a `.env` file in `src/main/resources/` or define them in your environment:

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/social_media_db
DB_USER=postgres
DB_PASS=your_super_secret_password

# Authentication
JWT_SECRET=your_base64_encoded_secure_string

# Storage
SUPABASE_URL=your_supabase_project_url
SUPABASE_KEY=your_supabase_anon_key

# External Services (Optional)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

### Running the Backend

```bash
mvn clean install
mvn spring-boot:run
```

The server will start on `http://localhost:8080`.
* **Health Check**: `http://localhost:8080/actuator/health`
* **Prometheus Metrics**: `http://localhost:8080/actuator/prometheus`

### Running the Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend will start on `http://localhost:5173`.

---
*Built for scale, secured by design.*
