package com.kan.util;

import com.kan.model.Employee;

import java.util.UUID;

/**
 * Factory helper to create Employee test fixtures.
 */
public final class EmployeeFactory {

    private EmployeeFactory() {}

    /** Creates a valid employee payload (no id – let API assign). */
    public static Employee validEmployee() {
        Employee e = new Employee();
        e.setName("John Doe " + UUID.randomUUID().toString().substring(0, 6));
        e.setEmail("john.doe." + UUID.randomUUID().toString().substring(0, 6) + "@example.com");
        e.setDepartment("Engineering");
        e.setSalary(75000.0);
        return e;
    }

    /** Creates an employee with a blank name to trigger validation errors. */
    public static Employee employeeWithBlankName() {
        Employee e = validEmployee();
        e.setName("");
        return e;
    }

    /** Creates an employee with an invalid email. */
    public static Employee employeeWithInvalidEmail() {
        Employee e = validEmployee();
        e.setEmail("not-an-email");
        return e;
    }

    /** Creates an employee with a negative salary. */
    public static Employee employeeWithNegativeSalary() {
        Employee e = validEmployee();
        e.setSalary(-1.0);
        return e;
    }
}
