# Pagila API

A Spring Boot REST API for the Pagila DVD rental database, featuring comprehensive CRUD operations, authentication, and authorization capabilities.

## Overview

The Pagila API is built on top of the popular Pagila sample database (PostgreSQL version of the Sakila database). It provides a modern REST API interface for managing a DVD rental store system with features including:

- **Complete CRUD operations** for all entities (Films, Actors, Staff, Customers, Rentals, etc.)
- **OAuth2 and JWT authentication** with Google integration 
- **Role-based authorization** 
- **Pagination and filtering** support
- **Docker containerization** for easy deployment
- **Comprehensive validation** and error handling

## Tech Stack

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Security** with OAuth2 & JWT
- **Spring Data JPA** with Hibernate
- **PostgreSQL** database
- **Docker & Docker Compose**
- **Maven** for dependency management
- **Lombok** for reducing boilerplate code

## API Endpoints

The API provides the following main resource endpoints:

### Core Resources
- `/api/films` - Film management (movies, ratings, categories)
- `/api/actors` - Actor information and filmography
- `/api/categories` - Film categories
- `/api/staff` - Staff member management
- `/api/stores` - Store locations and management
- `/api/inventory` - Inventory tracking
- `/api/rentals` - Rental transactions and history

### Authentication
- `/api/auth` - Authentication endpoints (login, token refresh, etc.)

Each endpoint supports standard REST operations (GET, POST, PUT, DELETE) with appropriate authorization checks.

## Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 17 (for local development)
- Maven 3.6+ (for local development)

### Running with Docker (Recommended)

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd pagila-api
   ```

2. **Start the application**
   ```bash
   docker-compose up -d
   ```

3. **Access the API**
   - API Base URL: `http://localhost:8080`
   - Database: `localhost:5433` (externally accessible)

### Running Locally

1. **Set up PostgreSQL**
   ```bash
   # Start only the database container
   docker-compose up -d db
   ```

2. **Configure application properties**
   Update `src/main/resources/application.yml` with your local settings if needed.

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

## Configuration

### Environment Variables

The application can be configured using the following environment variables:

#### Database Configuration
- `SPRING_DATASOURCE_URL` - Database connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username  
- `SPRING_DATASOURCE_PASSWORD` - Database password

#### OAuth2 Configuration
- `GOOGLE_CLIENT_ID` - Google OAuth2 client ID
- `GOOGLE_CLIENT_SECRET` - Google OAuth2 client secret
- `PAGILA_CLIENT_ID` - Custom OAuth2 client ID
- `PAGILA_CLIENT_SECRET` - Custom OAuth2 client secret

#### JWT Configuration
- `JWT_SECRET` - Secret key for JWT token signing
- `JWT_EXPIRATION` - Token expiration time in milliseconds

### Docker Compose Configuration

The `docker-compose.yml` file sets up:
- **Application container** on port 8080
- **PostgreSQL database** on port 5433 (external), 5432 (internal)
- **Persistent volume** for database data

## Database Schema

The application uses the Pagila database schema, which includes:

- **Film catalog** (films, categories, actors, film_actor relationships)
- **Store management** (stores, staff, addresses)
- **Customer management** (customers, addresses)
- **Inventory tracking** (inventory items per store)
- **Rental system** (rentals, payments)

## Authentication & Authorization

The API supports multiple authentication methods:

### OAuth2 Providers
- **Google OAuth2** - Social login integration
- **Custom OAuth2** - Internal authentication server

### JWT Tokens
- Stateless authentication using JWT
- Configurable expiration times
- Secure token signing

### Security Features
- Role-based access control
- Protected endpoints
- CORS configuration
- CSRF protection

## API Usage Examples

### Get all films with pagination
```bash
curl -X GET "http://localhost:8080/api/films?page=0&size=10"
```

### Get a specific actor
```bash
curl -X GET "http://localhost:8080/api/actors/1"
```

### Create a new rental (requires authentication)
```bash
curl -X POST "http://localhost:8080/api/rentals" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{"customerId": 1, "inventoryId": 1}'
```

## Development

### Project Structure
```
src/
├── main/
│   ├── java/com/example/pagila_api/
│   │   ├── controller/     # REST controllers
│   │   ├── service/        # Business logic
│   │   ├── repository/     # Data access layer
│   │   ├── model/          # Entity classes
│   │   ├── config/         # Configuration classes
│   │   └── PagilaApiApplication.java
│   └── resources/
│       ├── application.yml
│       ├── hibernate.cfg.xml
│       └── hibernate.reveng.xml
└── test/
    └── resources/
        ├── application.properties
        └── test-data.sql
```

### Building the Application
```bash
# Build with Maven
./mvnw clean package

# Build Docker image
docker build -t pagila-api .

# Run tests
./mvnw test
```

### Code Generation
The project includes Hibernate Tools configuration for reverse engineering database entities from the Pagila schema.

## Troubleshooting

### Common Issues

1. **Port 5432 already in use**
   - The docker-compose.yml maps PostgreSQL to port 5433 externally to avoid conflicts
   - If you still have issues, check for other PostgreSQL instances

2. **Authentication errors**
   - Verify OAuth2 client credentials are properly configured
   - Check JWT secret key configuration
   - Ensure proper scope permissions

3. **Database connection issues**
   - Verify database container is running: `docker-compose ps`
   - Check database logs: `docker-compose logs db`
   - Confirm connection parameters match between app and database

### Logs
```bash
# View application logs
docker-compose logs app

# View database logs  
docker-compose logs db

# Follow logs in real-time
docker-compose logs -f
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Built on the [Pagila sample database](https://github.com/devrimgunduz/pagila)
- Inspired by the MySQL Sakila sample database
- Spring Boot community for excellent documentation and examples