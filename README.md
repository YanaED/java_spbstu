# Spring Boot Microservices REST API

A production-ready microservices architecture with Spring Boot 3.2, featuring CRUD operations, caching, asynchronous processing, and monitoring.

## Architecture Overview

The application is split into three microservices:

1. **Task Service**: Manages tasks with CRUD operations, caching via Redis, and publishes events
2. **User Service**: Handles user management with CRUD operations
3. **Notification Service**: Subscribes to events and manages notifications

## Features

- **Spring Boot 3.2 + Gradle** multi-module project
- **RESTful APIs** with proper resource mapping
- **Database Integration**:
  - In-memory H2 database for development
  - PostgreSQL with Spring Data JPA for production
  - Flyway for database migrations
- **Caching with Redis** for improved performance
- **Messaging with RabbitMQ** for event-driven architecture
- **Asynchronous Processing** via Spring's @Async and @Scheduled
- **Monitoring with Spring Boot Actuator, Prometheus, and Grafana**
- **Docker and Docker Compose** for containerization
- **Comprehensive Test Coverage** with JUnit 5, Mockito, and Testcontainers

## Project Structure

```
spring-boot-microservices/
├── common/                      # Shared DTOs, events, and exceptions
├── task-service/                # Task microservice
├── user-service/                # User microservice
├── notification-service/        # Notification microservice
├── monitoring/                  # Prometheus and Grafana configuration
├── scripts/                     # Helper scripts
├── docker-compose.yml           # Docker compose configuration
├── .github/workflows/           # CI/CD workflows
└── README.md                    # Project documentation
```

## Prerequisites

- JDK 17+
- Gradle 8+ (or use the Gradle wrapper)
- Docker and Docker Compose

## Running the Application

### Development Mode (Local)

1. Clone the repository
2. Run each service individually with the H2 database:

```bash
./gradlew :task-service:bootRun
./gradlew :user-service:bootRun
./gradlew :notification-service:bootRun
```

### Production Mode (Docker)

1. Clone the repository
2. Build and run all services with Docker Compose:

```bash
docker-compose up -d
```

This will start:
- PostgreSQL databases for each service
- Redis for caching
- RabbitMQ for messaging
- All three microservices
- Prometheus and Grafana for monitoring

## API Endpoints

### Task Service (port 8081)

- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `GET /api/tasks/user/{userId}` - Get tasks by user ID
- `POST /api/tasks` - Create a new task
- `PUT /api/tasks/{id}` - Update a task
- `DELETE /api/tasks/{id}` - Delete a task

### User Service (port 8082)

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `POST /api/users` - Create a new user
- `PUT /api/users/{id}` - Update a user
- `DELETE /api/users/{id}` - Delete a user

### Notification Service (port 8083)

- `GET /api/notifications/user/{userId}` - Get all notifications for a user
- `GET /api/notifications/user/{userId}/unread` - Get unread notifications for a user
- `PUT /api/notifications/{id}/read` - Mark a notification as read

## Monitoring

- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)

## Running Tests

```bash
./gradlew test
```

For integration tests:

```bash
./gradlew integrationTest
```

## CI/CD Pipeline

The project includes GitHub Actions workflows for:
- Building and testing the application
- Running integration tests
- Building and pushing Docker images
- Deploying to a target environment

## Reactive Version

The project also includes a reactive version of the Task Service implemented with Spring WebFlux and R2DBC.

To run the reactive version:

```bash
./gradlew :reactive-task-service:bootRun
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.
