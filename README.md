# Microservice Order System

A microservice-based e-commerce order system built with Spring Boot and gRPC, demonstrating modern microservices architecture and best practices.

## Architecture

The system consists of three main microservices:
- **Store Service**: Handles order management and product inventory
- **Bank Service**: Manages payment processing and refunds
- **Email Service**: Handles email notifications

### Technology Stack

#### Backend
- Java 17
- Spring Boot
- MySQL
- MyBatis
- gRPC
- RESTful APIs

#### Communication
- gRPC (Bank Service Integration)
- REST API (Email Service Integration)

#### Database
- MySQL
- MyBatis ORM

#### Tools & Libraries
- Maven
- Protocol Buffers
- Spring Boot Starter
- gRPC Spring Boot Starter

## Features

- Order Management
- Payment Processing
- Refund Handling
- Inventory Management
- Email Notifications
- Transaction Management
- Account Balance Management

Project Structure
```
microservice-order-system/
├── store/                
├── bank-service/         
├── email-service/        
├── store/             
├── bank_service/         
├── scripts/             
│   └── startAll.sh       
└── README.md       

```



## Getting Started

### Prerequisites
- Java 17
- Maven
- MySQL

### Database Setup
The MySQL table schemas for the Store Service and Bank Service are provided in the root directory:

store_db/: Contains SQL scripts for the Store Service database.
bank_service/: Contains SQL scripts for the Bank Service database.


Starting the System
To start all microservices and set up the necessary components, use the provided startup script:

```
./scripts/startAll.sh
```
This script will:

Initialize the MySQL databases for the Store and Bank services.
Start the Store Service, Bank Service, and Email Service.
Ensure all services are up and running.