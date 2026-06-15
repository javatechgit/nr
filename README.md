# Retailer Rewards Program

A Spring Boot application that calculates reward points for customers based on their transaction history.

## Rewards Program Rules

- **2 points** for every dollar spent **over $100** in each transaction
- **1 point** for every dollar spent **between $50 and $100** in each transaction
- **0 points** for purchases of $50 or less

### Example Calculation
A $120 purchase:
- $20 over $100 × 2 points = 40 points
- $50 between $50-$100 × 1 point = 50 points
- **Total: 90 points**

## Project Structure

```
src/
├── main/
│   ├── java/com/retailer/rewards/
│   │   ├── RewardsApplication.java          # Main Spring Boot application
│   │   ├── controller/
│   │   │   └── RewardsController.java        # REST API endpoints
│   │   ├── model/
│   │   │   ├── Customer.java                 # Customer entity
│   │   │   ├── Transaction.java              # Transaction entity
│   │   │   └── RewardPoints.java             # Reward points DTO
│   │   ├── repository/
│   │   │   ├── CustomerRepository.java       # Customer data access
│   │   │   └── TransactionRepository.java    # Transaction data access
│   │   └── service/
│   │       ├── RewardsCalculator.java        # Points calculation logic
│   │       └── RewardsService.java           # Business logic
│   └── resources/
│       └── application.properties            # Configuration
└── test/
    └── java/com/retailer/rewards/
        └── service/
            └── RewardsCalculatorTest.java    # Unit tests
```

## Technology Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **H2 Database** (for development)
- **Lombok** (for reducing boilerplate)
- **Maven**

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Calculate Rewards for Date Range
```
GET /api/rewards/calculate?startDate=2024-01-01T00:00:00&endDate=2024-03-31T23:59:59
```
Returns reward points for all customers during the specified period, broken down by month.

### Get Total Points for a Customer
```
GET /api/rewards/customer/{customerId}/total
```
Returns the total accumulated reward points for a specific customer.

### Add a Transaction
```
POST /api/rewards/customer/{customerId}/transaction
Content-Type: application/json

{
  "amount": 120.00,
  "transactionDate": "2024-01-15T10:30:00"
}
```
Adds a new transaction for a customer and calculates earned points.

## Example Usage

### 1. Calculate rewards for a three-month period
```bash
curl "http://localhost:8080/api/rewards/calculate?startDate=2024-01-01T00:00:00&endDate=2024-03-31T23:59:59"
```

Response:
```json
[
  {
    "customerId": "CUST001",
    "customerName": "John Doe",
    "month": 1,
    "year": 2024,
    "monthlyPoints": 120,
    "totalPoints": 350
  },
  {
    "customerId": "CUST001",
    "customerName": "John Doe",
    "month": 2,
    "year": 2024,
    "monthlyPoints": 110,
    "totalPoints": 350
  },
  {
    "customerId": "CUST001",
    "customerName": "John Doe",
    "month": 3,
    "year": 2024,
    "monthlyPoints": 120,
    "totalPoints": 350
  }
]
```

## Testing

Run the unit tests:
```bash
mvn test
```

## Database

The application uses H2 database for development. You can access the H2 console at:
```
http://localhost:8080/h2-console
```

Default credentials:
- URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

## Database Schema

### Customers Table
```sql
CREATE TABLE customers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  customer_id VARCHAR(255) UNIQUE NOT NULL,
  name VARCHAR(255) NOT NULL
);
```

### Transactions Table
```sql
CREATE TABLE transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  customer_id BIGINT NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  transaction_date TIMESTAMP NOT NULL,
  points_earned INT NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customers(id)
);
```

## Future Enhancements

- [ ] Add pagination to API responses
- [ ] Add CSV import for bulk transaction loading
- [ ] Add filtering by customer, month, or year
- [ ] Add redemption functionality
- [ ] Add authentication and authorization
- [ ] Add transaction history UI
- [ ] Add reporting and analytics
- [ ] Add database persistence (PostgreSQL, MySQL)
- [ ] Add API documentation with Swagger/OpenAPI
- [ ] Add comprehensive integration tests

## License

This project is licensed under the MIT License.