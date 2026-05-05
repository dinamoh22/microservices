package org.exampl;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    Employee create(Employee employee);
    Employee findById(Long id);
    Employee findByEmail(String email);
    Employee replace(Long id, Employee newEmployee);
    void deleteByIdOrThrow(Long id);
   // Employee assignToDepartment(Long employeeId, Long deptId);
    Employee updateExisting(Long id, Employee newEmployee);
    //PagedResponse<EmployeeResponseDto> listEmployees(Pageable pageable);
//    PagedResponse<EmployeeResponseDto> listEmployees(
//            Pageable pageable,
//            String role,
//            String nameContains,
//            String emailContains,
//            Integer minExperience,
//            Integer maxExperience,
//            LocalDate hiredAfter,
//            LocalDate hiredBefore
//    );

}
