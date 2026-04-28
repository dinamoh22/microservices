package com.lecture.departmentsservice;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/departments")
public class DepartmentInternalController {

    private final DepartmentService departmentService;

    public DepartmentInternalController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/summary")
    public DepartmentSummaryResponse getSummary(@PathVariable Long id) {
        Department department = departmentService.findByIdOrThrow(id);
        return new DepartmentSummaryResponse(department.getId(), department.getName());
    }
}
