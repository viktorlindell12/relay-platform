# Relay Platform

> Distributed communication platform for development teams

## Architecture

```text
┌─────────────┐     REST      ┌──────────────────┐
│   Frontend  │ ────────────► │   relay-bff       │
│  React/Vite │               │   :8080           │
└─────────────┘               └────────┬──────────┘
                                       │ REST
                    ┌──────────────────┼───────────────────┐
                    ▼                  ▼                   ▼
           ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐
           │  relay-auth  │  │  relay-user  │  │  relay-message   │
           │  :8081       │  │  :8082       │  │  :8083           │
           │  auth_db     │  │  user_db     │  │  message_db      │
           └──────────────┘  └──────┬───────┘  └────────┬─────────┘
                                    ▲  gRPC              │ AMQP
                                    └────────────────────┘
                                                         │
                                                ┌────────▼────────┐
                                                │    RabbitMQ     │
                                                │  relay.events   │
                                                └────────┬────────┘
                                                         │ message.published
                                                ┌────────▼────────┐
                                                │   relay-bot     │
                                                │   :8084         │
                                                └─────────────────┘
```

## Tech Stack

| Service              | Language | Framework        | Database   |
|----------------------|----------|------------------|------------|
| relay-bff-service    | Java 25  | Spring Boot 4.0  | —          |
| relay-auth-service   | Java 25  | Spring Boot 4.0  | PostgreSQL |
| relay-user-service   | Java 25  | Spring Boot 4.0  | PostgreSQL |
| relay-message-service| Java 25  | Spring Boot 4.0  | PostgreSQL |
| relay-bot-service    | Java 25  | Spring Boot 4.0  | —          |
| relay-frontend       | TypeScript| React + Vite    | —          |

**Message Queue:** RabbitMQ  
**Auth:** JWT (JJWT 0.12)  
**Internal RPC:** gRPC (Message Service → User Service)  
**Container:** Docker + Docker Compose  
**Orchestration:** Kubernetes (Minikube)

## Services

| Service | Responsibility |
|---------|----------------|
| **BFF** | Single entry point. Validates JWT, routes to internal services. |
| **Auth** | Registration, login, JWT issuance. |
| **User** | User profile CRUD. Exposes `GetUserById` via gRPC. |
| **Message** | Stores messages. Publishes `message.published` to RabbitMQ. |
| **Bot** | Consumes events, replies automatically via Message Service. |

## Event Flow

```text
Client sends message
  → BFF validates JWT
  → Message Service saves to message_db
  → Message Service publishes message.published to RabbitMQ
  → Bot Service consumes event
  → Bot Service posts reply via Message Service
```

## Ports

| Service              | HTTP  | gRPC |
|----------------------|-------|------|
| relay-bff-service    | 8080  | —    |
| relay-auth-service   | 8081  | —    |
| relay-user-service   | 8082  | 9090 |
| relay-message-service| 8083  | —    |
| relay-bot-service    | 8084  | —    |

## Getting Started

### Prerequisites

- Java 25
- Docker + Docker Compose
- Node.js 22+

### Run with Docker Compose

```bash
docker-compose up
```

API available at `http://localhost:8080`  
Swagger UI at `http://localhost:8080/swagger-ui.html`  
RabbitMQ Management at `http://localhost:15672` (guest/guest)

### Run locally (development)

Start infrastructure first, then each service in its own terminal tab:

```bash
# Terminal 1 — infrastructure
docker-compose up -d auth-db user-db message-db rabbitmq
```

```bash
# Terminal 2-6 — one per service (run from repo root)
mvn spring-boot:run -f relay-auth-service/pom.xml
mvn spring-boot:run -f relay-user-service/pom.xml
mvn spring-boot:run -f relay-message-service/pom.xml
mvn spring-boot:run -f relay-bff-service/pom.xml
mvn spring-boot:run -f relay-bot-service/pom.xml
```

### Kubernetes (Minikube)

```bash
# Coming in final milestone
minikube start
kubectl apply -f k8s/
```

## API Overview

| Method | Endpoint               | Auth     | Description          |
|--------|------------------------|----------|----------------------|
| POST   | /api/auth/register     | Public   | Register user        |
| POST   | /api/auth/login        | Public   | Login, returns JWT   |
| GET    | /api/users/{id}        | Required | Get user profile     |
| POST   | /api/messages          | Required | Send message         |
| GET    | /api/messages          | Required | Get messages         |