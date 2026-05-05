package com.lecture.employeesservice;

import com.lecture.employeesservice.dtos.EmployeeRequestDto;
import com.lecture.employeesservice.dtos.EmployeeResponseDto;

public class EmployeeMapper {

    private EmployeeMapper() {}

    public static Employee toEntity(EmployeeRequestDto dto) {
        Employee e = new Employee();
        e.setName(dto.getName());
        e.setRole(dto.getRole());
        e.setEmail(dto.getEmail());
        e.setDepartmentId(dto.getDepartmentId());
        return e;
    }

    public static EmployeeResponseDto toDto(Employee e) {
        return new EmployeeResponseDto(
                e.getId(),
                e.getName(),
                e.getRole(),
                e.getEmail(),
                e.getDepartmentId() != null ? "Department " + e.getDepartmentId() : null
        );
    }
}
