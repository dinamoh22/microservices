package org.exampl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/departments")
public class DepartmentInternalController {

    private final DepartmentRepository departmentRepository;

    public DepartmentInternalController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * Internal endpoint called by employees-service.
     * Any authenticated user (whose token is propagated) can call this.
     * Uses repository directly to bypass the ADMIN-only check on DepartmentService.findById,
     * allowing regular user tokens forwarded from employees-service to work here.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/summary")
    public DepartmentSummaryResponse getSummary(@PathVariable Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
        return new DepartmentSummaryResponse(department.getId(), department.getName());
    }
}
