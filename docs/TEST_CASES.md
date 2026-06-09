# KAN-1 — Employee API: Test Cases

## User Story
**As** an API consumer,  
**I want** to manage employee records via a REST API,  
**So that** I can Create, Read, Update, and Delete (CRUD) employee data programmatically.

---

## Test Cases

| TC ID  | Title                                   | Endpoint                      | Method | Input                          | Expected Status | Expected Response                          |
|--------|-----------------------------------------|-------------------------------|--------|--------------------------------|-----------------|--------------------------------------------|
| TC-01  | List all employees                      | `/api/employees`              | GET    | —                              | 200             | JSON array (may be empty)                  |
| TC-02  | Create a valid employee                 | `/api/employees`              | POST   | Valid employee JSON            | 201             | Employee JSON with generated `id`          |
| TC-03  | Get employee by ID                      | `/api/employees/{id}`         | GET    | Valid `id`                     | 200             | Matching employee JSON                     |
| TC-04  | Update employee by ID                   | `/api/employees/{id}`         | PUT    | Updated employee JSON          | 200             | Employee JSON with updated fields          |
| TC-05  | Delete employee by ID                   | `/api/employees/{id}`         | DELETE | Valid `id`                     | 204             | Empty body                                 |
| TC-06  | Get non-existent employee               | `/api/employees/99999`        | GET    | Non-existent `id`              | 404             | Error message                              |
| TC-07  | Create employee with blank name         | `/api/employees`              | POST   | `name: ""`                     | 400             | Validation error                           |
| TC-08  | Create employee with invalid email      | `/api/employees`              | POST   | `email: "not-an-email"`        | 400             | Validation error                           |
| TC-09  | Create employee with negative salary    | `/api/employees`              | POST   | `salary: -1`                   | 400             | Validation error                           |
| TC-10  | Delete non-existent employee            | `/api/employees/99999`        | DELETE | Non-existent `id`              | 404             | Error message                              |

---

## Running Tests

```bash
# Run all tests
mvn test

# Run against a custom API base URL
mvn test -Dapi.base.url=https://your-api-host.com
```

---

## Framework Stack

| Component   | Library / Version         |
|-------------|---------------------------|
| Language    | Java 17                   |
| HTTP Client | REST Assured 5.5.0        |
| Test Runner | TestNG 7.10.2             |
| Assertions  | Hamcrest 2.2              |
| JSON Model  | Jackson 2.17.2            |
| Build       | Maven 3 + Surefire 3.5.2  |
