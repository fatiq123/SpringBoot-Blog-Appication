# Spring Boot Blog REST API - Documentation

This document provides a comprehensive overview of the Spring Boot Blog REST API implementation.

## Architecture Overview

The application follows a layered architecture:
- Controllers (REST API endpoints)
- Services (Business logic)
- Repositories (Data access)
- Security (JWT Authentication)
- Configuration
- DTOs/Entities

## Key Components

### 1. Security Implementation
- JWT-based authentication
- Role-based authorization (ADMIN and USER roles)
- Secure password encoding
- Protected endpoints using @PreAuthorize

### 2. Post Management
- CRUD operations for blog posts
- Pagination and sorting support
- Category-based filtering
- Input validation
- Admin-only operations for create/update/delete

### 3. Authentication
- Login/Signin endpoints
- Registration/Signup functionality
- JWT token generation and validation

### 4. Swagger/OpenAPI Documentation
- API documentation with security schemes
- Detailed endpoint descriptions
- Request/Response examples

## API Endpoints

### Authentication APIs
```
POST /api/auth/login     - User login
POST /api/auth/register  - User registration
```

### Post APIs
```
POST   /api/posts        - Create new post (ADMIN only)
GET    /api/posts        - Get all posts (paginated)
GET    /api/posts/{id}   - Get post by ID
PUT    /api/posts/{id}   - Update post (ADMIN only)
DELETE /api/posts/{id}   - Delete post (ADMIN only)
GET    /api/posts/category/{id} - Get posts by category
```

## Security Configuration

The application uses JWT (JSON Web Tokens) for authentication with the following features:
- Secure secret key configuration
- 7-day token expiration
- Bearer token authentication
- CORS configuration
- Role-based endpoint security

## Database Configuration

Different profiles (dev, qa, prod) for database configuration:
- MySQL database
- JPA/Hibernate for ORM
- Automatic schema updates
- Connection pooling

## How to Use

1. Start by registering a new user using the /api/auth/register endpoint
2. Login with credentials to get JWT token
3. Use the token in the Authorization header for subsequent requests
4. Access protected endpoints with proper roles

## Exception Handling

The API includes global exception handling for:
- Resource not found
- Blog API exceptions
- Validation errors
- Authentication errors

## Application Properties

Key configurations in application.properties:
- JWT secret and expiration
- Database connections
- Active profile selection
- Logging levels

## Additional Features

- Model mapping with ModelMapper
- Input validation using @Valid
- Swagger UI integration
- Proper response status codes
- Pagination metadata

This implementation provides a secure, scalable, and well-structured blog API with proper
authentication, authorization, and data management capabilities.
