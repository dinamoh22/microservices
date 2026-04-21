package com.lecture.employeesservice;

public class EmployeeNotFoundException extends RuntimeException {

    EmployeeNotFoundException(Long id) {
        super("Could not find employee " + id);
    }

    EmployeeNotFoundException(String email) {
        super("Could not find employee " + email);
    }
}