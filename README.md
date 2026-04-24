# Smart Auth Gateway

A production-style API gateway built with Spring Boot that handles authentication, authorisation, and rate limiting for downstream services.

## What it does

- **JWT-based authentication** — users register/login and receive a signed token
- **Role-based access control** — USER and ADMIN roles with different endpoint access
- **Token blacklisting** — logout immediately invalidates the token via Redis
- **Rate limiting** — each IP is limited to 10 requests per minute, enforced via Redis
- **Database migrations** — schema managed by Flyway

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.2, Spring Security |
| Auth | JWT (jjwt), BCrypt password hashing |
| Database | PostgreSQL 16 |
| Cache / Rate Limit | Redis 7 |
| Migrations | Flyway |
| Runtime | Java 21 |
| Containers | Docker, Docker Compose |

## Run locally (one command)

Make sure you have Docker installed, then:

```bash
docker-compose up --build
```

The app starts on `http://localhost:8080`

## API Endpoints

### Auth
| Method | Endpoint | Auth required |
|--------|----------|--------------|
| POST | `/api/auth/register` | No |
| POST | `/api/auth/login` | No |
| POST | `/api/auth/logout` | Yes (Bearer token) |

### Resources
| Method | Endpoint | Role required |
|--------|----------|--------------|
| GET | `/api/public/hello` | None |
| GET | `/api/user/profile` | USER |
| GET | `/api/admin/dashboard` | ADMIN |

## Example usage

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "nivedha", "password": "secret123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "nivedha", "password": "secret123"}'

# Access protected route
curl http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer <your-token-here>"

# Logout (blacklists the token)
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer <your-token-here>"
```

## Project Structure

```
src/main/java/com/nivedha/gateway/
├── config/         # Security and Redis configuration
├── controller/     # Auth and resource endpoints
├── filter/         # JWT filter with rate limiting
├── model/          # User entity
├── repository/     # Database access
└── service/        # JWT, blacklist, rate limiter logic
```
