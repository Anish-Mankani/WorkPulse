# WorkPulse API 🚀

A production-ready **Workflow Task Management REST API** built with Spring Boot, featuring JWT authentication, role-based access control, and a full task approval workflow.

---

## Features

- JWT Authentication & Authorization
- Role-based access control (USER, MANAGER, ADMIN)
- Task approval workflow (DRAFT → SUBMITTED → APPROVED/REJECTED → COMPLETED)
- Password encryption with BCrypt
- DTO pattern (no sensitive data exposure)
- PostgreSQL database with JPA/Hibernate
- Clean service layer with business logic validation

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Core language |
| Spring Boot 3 | Framework |
| Spring Security | Authentication & Authorization |
| JWT (jjwt 0.11.5) | Token-based auth |
| PostgreSQL | Database |
| Spring Data JPA | Database layer |
| Hibernate | ORM |
| Lombok | Boilerplate reduction |
| Maven | Build tool |

---

## Project Structure

```
com.workpulse
├── config
│   ├── SecurityConfig.java
│   └── JwtFilter.java
├── controller
│   ├── UserController.java
│   └── TaskController.java
├── dto
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── UserResponse.java
│   ├── TaskRequest.java
│   └── TaskResponse.java
├── model
│   ├── AppUser.java
│   ├── Task.java
│   ├── UserRole.java
│   └── TaskStatus.java
├── repository
│   ├── AppUserRepository.java
│   └── TaskRepository.java
├── service
│   ├── AppUserService.java
│   ├── TaskService.java
│   └── CustomUserDetailsService.java
└── util
    └── JwtUtil.java
```

---

## Role Permissions

| Action | USER | MANAGER | ADMIN |
|--------|------|---------|-------|
| Register/Login | ✅ | ✅ | ✅ |
| Create Task    | ✅ | ✅ | ✅ |
| Submit Task    | ✅ | ✅ | ✅ |
| Approve Task   | ❌ | ✅ | ✅ |
| Reject Task    | ❌ | ✅ | ✅ |
| Complete Task  | ✅ | ✅ | ✅ |
| View Own Tasks | ✅ | ✅ | ✅ |
| View All Tasks | ❌ | ❌ | ✅ |
| Manage Users   | ❌ | ❌ | ✅ |
| Update Roles   | ❌ | ❌ | ✅ |

---

## Task Workflow

```
USER creates task     →  DRAFT
USER submits task     →  SUBMITTED
MANAGER approves      →  APPROVED
MANAGER rejects       →  REJECTED (can resubmit)
USER completes task   →  COMPLETED
```

---

## Setup & Installation

### Prerequisites
- Java 17+
- PostgreSQL
- Maven

### Steps

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/workpulse.git
cd workpulse
```

2. **Create PostgreSQL database**
```sql
CREATE DATABASE workpulse;
```

3. **Configure application properties**
```bash
cp application.properties.example src/main/resources/application.properties
```

Fill in your credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/workpulse
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_secret_key_here
jwt.expiration=86400000
```

4. **Run the application**
```bash
mvn spring-boot:run
```

API will start at `http://localhost:8080`

---

## API Endpoints

### Authentication (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/user/register` | Register new user |
| POST | `/api/user/login` | Login and get JWT token |

### Users (Protected)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET | `/api/user` | ADMIN | Get all users |
| GET | `/api/user/{id}` | ALL | Get user by ID |
| PUT | `/api/user/{id}/role` | ADMIN | Update user role |
| DELETE | `/api/user/{id}` | ADMIN | Delete user |

### Tasks (Protected)

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/tasks` | USER | Create task |
| GET | `/api/tasks` | ADMIN | Get all tasks |
| GET | `/api/tasks/user/{userId}` | ALL | Get tasks by user |
| GET | `/api/tasks/pending` | MANAGER | Get pending tasks |
| GET | `/api/tasks/completed` | ALL | Get completed tasks |
| PUT | `/api/tasks/{id}/submit` | USER | Submit task |
| PUT | `/api/tasks/{id}/approve` | MANAGER | Approve task |
| PUT | `/api/tasks/{id}/reject` | MANAGER | Reject task |
| PUT | `/api/tasks/{id}/complete` | USER | Complete task |
| DELETE | `/api/tasks/{id}` | ADMIN | Delete task |

---

## Authentication

All protected endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

### Example Flow

**1. Register:**
```json
POST /api/user/register
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "secret123"
}
```

**2. Login:**
```json
POST /api/user/login
{
    "email": "john@example.com",
    "password": "secret123"
}

Response:
"eyJhbGciOiJIUzI1NiJ9..."
```

**3. Use token:**
```
GET /api/tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Error Responses

All errors return a consistent JSON format:

```json
{
    "status": 404,
    "message": "Task not found",
    "timestamp": "2026-04-10T17:31:35.582544"
}
```

---

## Author

Built by **Anish Mankani** as a portfolio project to demonstrate Spring Boot, JWT, and REST API development skills.

---

## License

This project is open source and available under the [MIT License](LICENSE).
