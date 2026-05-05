package org.exampl;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long id) {

        super("Could not find department " + id);
    }

    DepartmentNotFoundException(String location) {

        super("Could not find department " + location);
    }
}
