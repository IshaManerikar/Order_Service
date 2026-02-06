# Order Service

Spring Boot microservice responsible for managing orders and communicating with User Service.

## 🚀 Tech Stack
- Java
- Spring Boot
- MySQL
- Hibernate / JPA
- WebClient
- Maven

## 📌 Features
- Order CRUD APIs
- Soft delete and restore
- Order Lifecycle maintains (CREATED->PAID->SHIPPED->DELIVERED->CANCELLED)
- Validates user via User Service before creating orders
- Microservice-to-microservice communication using WebClient

## 🛠 Setup

1. Clone repository

git clone <repo-url>

2. Configure database

Create file:

src/main/resources/application.properties

Use application.properties.example as reference.

3. Run application

mvn spring-boot:run

Server starts on:

http://localhost:8082

## 🔗 Related Service

Order Service communicates with User Service:

http://localhost:9091

