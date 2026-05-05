package com.lecture.employeesservice;

import java.util.List;

import com.lecture.employeesservice.dtos.EmployeeResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository repository;
    private final DepartmentRestClient departmentRestClient;

    public EmployeeServiceImpl(EmployeeRepository repository, DepartmentRestClient departmentRestClient) {
        this.repository = repository;
        this.departmentRestClient = departmentRestClient;
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }

    public Employee create(Employee employee){
        validateDepartment(employee.getDepartmentId());
        return repository.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotFoundException(email));
    }

    // Update-only: throws 404 if not found
    public Employee updateExisting(Long id, Employee newEmployee) {
        validateDepartment(newEmployee.getDepartmentId());

        Employee existing = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        existing.setName(newEmployee.getName());
        existing.setRole(newEmployee.getRole());
        existing.setEmail(newEmployee.getEmail());

        return repository.save(existing);
    }

    public void deleteByIdOrThrow(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public PagedResponse<EmployeeResponseDto> listEmployees(
            Pageable pageable,
            String role,
            String nameContains
    ) {
        // start with a no-op specification using a conjunction predicate to avoid null
        Specification<Employee> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (role != null && !role.isBlank()) {
            spec = spec.and(EmployeeSpecifications.hasRole(role));
        }
        if (nameContains != null && !nameContains.isBlank()) {
            spec = spec.and(EmployeeSpecifications.nameContains(nameContains));
        }

        Page<Employee> page = repository.findAll(spec, pageable);

        log.info("Employees query -> totalElements={}, numberOfElements={}, page={}, size={}",
                page.getTotalElements(),
                page.getNumberOfElements(),
                page.getNumber(),
                page.getSize());

        List<EmployeeResponseDto> content = page.getContent()
                .stream()
                .map(EmployeeMapper::toDto)
                .toList();

        return PagedResponse.from(page, content);
    }


    private void validateDepartment(Long departmentId) {
        try {
            departmentRestClient.getDepartmentSummary(departmentId);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new BadRequestException("Invalid departmentId: " + departmentId);
        } catch (RestClientException ex) {
            throw new DownstreamServiceException("Departments service is unavailable");
        }
    }


}
