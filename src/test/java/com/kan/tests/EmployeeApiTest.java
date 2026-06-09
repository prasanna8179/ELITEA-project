package com.kan.tests;

import com.kan.config.ApiConfig;
import com.kan.model.Employee;
import com.kan.util.EmployeeFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * API Test Suite for KAN-1: Employee CRUD operations.
 *
 * Test Cases:
 *  TC-01  GET  /api/employees        -> 200, non-empty array
 *  TC-02  POST /api/employees        -> 201, created employee returned
 *  TC-03  GET  /api/employees/{id}   -> 200, correct employee
 *  TC-04  PUT  /api/employees/{id}   -> 200, updated fields reflected
 *  TC-05  DELETE /api/employees/{id} -> 204, resource removed
 *  TC-06  GET  /api/employees/99999  -> 404
 *  TC-07  POST /api/employees (blank name) -> 400
 *  TC-08  POST /api/employees (invalid email) -> 400
 *  TC-09  POST /api/employees (negative salary) -> 400
 *  TC-10  DELETE /api/employees/99999 -> 404
 */
public class EmployeeApiTest {

    private RequestSpecification spec;
    private int createdEmployeeId;

    @BeforeClass
    public void setUp() {
        ApiConfig.configure();
        spec = ApiConfig.buildRequestSpec();
    }

    // ─── TC-01: List all employees ────────────────────────────────────────────

    @Test(description = "TC-01: GET /api/employees returns 200 and a JSON array")
    public void tc01_getAllEmployees_returns200AndArray() {
        given(spec)
            .when()
                .get(ApiConfig.EMPLOYEES_PATH)
            .then()
                .statusCode(200)
                .contentType("application/json")
                .body("$", isA(java.util.List.class));
    }

    // ─── TC-02: Create employee ───────────────────────────────────────────────

    @Test(description = "TC-02: POST /api/employees creates employee and returns 201")
    public void tc02_createEmployee_returns201WithBody() {
        Employee payload = EmployeeFactory.validEmployee();

        Response response = given(spec)
                .body(payload)
            .when()
                .post(ApiConfig.EMPLOYEES_PATH)
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo(payload.getName()))
                .body("email", equalTo(payload.getEmail()))
                .body("department", equalTo(payload.getDepartment()))
                .body("salary", equalTo(payload.getSalary().floatValue()))
                .extract().response();

        createdEmployeeId = response.jsonPath().getInt("id");
    }

    // ─── TC-03: Get employee by ID ────────────────────────────────────────────

    @Test(
        description = "TC-03: GET /api/employees/{id} returns 200 and correct employee",
        dependsOnMethods = "tc02_createEmployee_returns201WithBody"
    )
    public void tc03_getEmployeeById_returns200() {
        given(spec)
            .when()
                .get(ApiConfig.EMPLOYEES_PATH + "/" + createdEmployeeId)
            .then()
                .statusCode(200)
                .body("id", equalTo(createdEmployeeId));
    }

    // ─── TC-04: Update employee ───────────────────────────────────────────────

    @Test(
        description = "TC-04: PUT /api/employees/{id} updates employee and returns 200",
        dependsOnMethods = "tc03_getEmployeeById_returns200"
    )
    public void tc04_updateEmployee_returns200WithUpdatedFields() {
        Employee update = EmployeeFactory.validEmployee();
        update.setName("Updated Employee");
        update.setDepartment("HR");
        update.setSalary(90000.0);

        given(spec)
                .body(update)
            .when()
                .put(ApiConfig.EMPLOYEES_PATH + "/" + createdEmployeeId)
            .then()
                .statusCode(200)
                .body("name", equalTo("Updated Employee"))
                .body("department", equalTo("HR"))
                .body("salary", equalTo(90000.0f));
    }

    // ─── TC-05: Delete employee ───────────────────────────────────────────────

    @Test(
        description = "TC-05: DELETE /api/employees/{id} returns 204",
        dependsOnMethods = "tc04_updateEmployee_returns200WithUpdatedFields"
    )
    public void tc05_deleteEmployee_returns204() {
        given(spec)
            .when()
                .delete(ApiConfig.EMPLOYEES_PATH + "/" + createdEmployeeId)
            .then()
                .statusCode(204);
    }

    // ─── TC-06: Get non-existent employee ────────────────────────────────────

    @Test(description = "TC-06: GET /api/employees/99999 returns 404")
    public void tc06_getNonExistentEmployee_returns404() {
        given(spec)
            .when()
                .get(ApiConfig.EMPLOYEES_PATH + "/99999")
            .then()
                .statusCode(404);
    }

    // ─── TC-07: Create employee with blank name ───────────────────────────────

    @Test(description = "TC-07: POST /api/employees with blank name returns 400")
    public void tc07_createEmployeeBlankName_returns400() {
        given(spec)
                .body(EmployeeFactory.employeeWithBlankName())
            .when()
                .post(ApiConfig.EMPLOYEES_PATH)
            .then()
                .statusCode(400);
    }

    // ─── TC-08: Create employee with invalid email ────────────────────────────

    @Test(description = "TC-08: POST /api/employees with invalid email returns 400")
    public void tc08_createEmployeeInvalidEmail_returns400() {
        given(spec)
                .body(EmployeeFactory.employeeWithInvalidEmail())
            .when()
                .post(ApiConfig.EMPLOYEES_PATH)
            .then()
                .statusCode(400);
    }

    // ─── TC-09: Create employee with negative salary ──────────────────────────

    @Test(description = "TC-09: POST /api/employees with negative salary returns 400")
    public void tc09_createEmployeeNegativeSalary_returns400() {
        given(spec)
                .body(EmployeeFactory.employeeWithNegativeSalary())
            .when()
                .post(ApiConfig.EMPLOYEES_PATH)
            .then()
                .statusCode(400);
    }

    // ─── TC-10: Delete non-existent employee ─────────────────────────────────

    @Test(description = "TC-10: DELETE /api/employees/99999 returns 404")
    public void tc10_deleteNonExistentEmployee_returns404() {
        given(spec)
            .when()
                .delete(ApiConfig.EMPLOYEES_PATH + "/99999")
            .then()
                .statusCode(404);
    }
}
