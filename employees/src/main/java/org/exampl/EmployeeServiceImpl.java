package org.exampl;

import java.time.LocalDate;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService  {

    private final EmployeeRepository repository;
    private final DepartmentRestClient departmentRestClient;
   // private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(EmployeeRepository repository, DepartmentRestClient departmentRestClient) {
        this.repository = repository;
        this.departmentRestClient = departmentRestClient;
       // this.departmentRepository = departmentRepository;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Employee create(Employee employee) {
        // Optional: ensure client doesn't control ID
        // employee.setId(null);

        return repository.save(employee);
    }


    public EmployeeResponseDto create2(EmployeeRequestDto request) {
        validateDepartment(request.getDepartmentId());

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setRole(request.getRole());
        employee.setSalary(request.getSalary());
        employee.setYearsOfExperience(request.getYearsOfExperience());
        employee.setHiredDate(request.getHiredDate());
        employee.setDepartmentId(request.getDepartmentId());

        repository.save(employee);

        return EmployeeMapper.toDto(employee);
    }


    private void validateDepartment(Long departmentId) {
        try {
            departmentRestClient.getDepartmentSummary(departmentId);
        } catch (DepartmentNotFoundException ex) {
            throw new InvalidDepartmentException("Invalid departmentId: " + departmentId);
        } catch (Exception ex) {
            throw new ServiceUnavailableException("Departments service is unavailable");
        }
    }




    @Transactional(readOnly = true)
    public EmployeeResponseDto getByIdWithDepartment(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        String departmentName = null;
        try {
            DepartmentSummaryDto dept = departmentRestClient.getDepartmentSummary(employee.getDepartmentId());
            departmentName = dept.name(); // لأن DTO record
        } catch (Exception ex) {
            // فشل enrichment → departmentName يبقى null
        }

        return EmployeeMapper.toDto(employee, departmentName); // تحتاج overload في Mapper
    }






    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or @employeeSecurity.isSelf(#id, authentication)")
    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
    @Override
    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotFoundException(email));
    }

    @Override
    public Employee replace(Long id, Employee newEmployee) {
        return null;
    }

    // Update-only: throws 404 if not found
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Employee updateExisting(Long id, Employee newEmployee) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        existing.setName(newEmployee.getName());
        existing.setRole(newEmployee.getRole());
        // If you have email:
         existing.setEmail(newEmployee.getEmail());

        return repository.save(existing);
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteByIdOrThrow(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
    }



//    @Override
//    public Employee assignToDepartment(Long employeeId, Long deptId) {
//        // 1. Find the employee (404 if missing)
//        Employee employee = repository.findById(employeeId)
//                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
//
//        // 2. Find the department (404 if missing)
//        Department department = departmentRepository.findById(deptId)
//                .orElseThrow(() -> new DepartmentNotFoundException(deptId));
//
//        // 3. Set the relationship
//        employee.setDepartment(department);
//
//        // 4. Save and return
//        return repository.save(employee);
//    }

//    @Override
//    public PagedResponse<EmployeeResponseDto> listEmployees(Pageable pageable) {
//
//
//        Page<Employee> page = repository.findAll(pageable);
//
//
//        List<EmployeeResponseDto> mappedContent = page.getContent()
//                .stream()
//                .map(EmployeeMapper::toDto)
//                .toList();
//
//
//        return PagedResponse.from(page, mappedContent);
//    }



    public PagedResponse<EmployeeResponseDto> listEmployees(
            Pageable pageable,
            String role,
            String nameContains,
            String emailContains,
            Integer minExperience,
            Integer maxExperience,
            LocalDate hiredAfter,
            LocalDate hiredBefore
    ) {
        // 1️⃣ نبدأ بـ spec فارغ
        Specification<Employee> spec = null;

        // 2️⃣ نضيف فلتر role
        if (role != null && !role.isBlank()) {
            spec = EmployeeSpecifications.hasRole(role);
        }

        // 3️⃣ نضيف فلتر nameContains
        if (nameContains != null && !nameContains.isBlank()) {
            Specification<Employee> nameSpec = EmployeeSpecifications.nameContains(nameContains);
            spec = (spec == null) ? nameSpec : spec.and(nameSpec);
        }

        if (emailContains != null && !emailContains.isBlank()) { // ✅ الجديد
            Specification<Employee> emailSpec = EmployeeSpecifications.emailContains(emailContains);
            spec = (spec == null) ? emailSpec : spec.and(emailSpec);
        }

        // 4️⃣ نضيف فلتر yearsOfExperience
        if (minExperience != null || maxExperience != null) {
            Specification<Employee> expSpec = EmployeeSpecifications.experienceBetween(minExperience, maxExperience);
            spec = (spec == null) ? expSpec : spec.and(expSpec);
        }

        // 5️⃣ نضيف فلتر hiredDate
        if (hiredAfter != null || hiredBefore != null) {
            Specification<Employee> dateSpec = EmployeeSpecifications.hiredDateBetween(hiredAfter, hiredBefore);
            spec = (spec == null) ? dateSpec : spec.and(dateSpec);
        }

        // 6️⃣ نجيب الصفحة من الريبو باستخدام الـ spec
        Page<Employee> page = repository.findAll(spec, pageable);

        // 7️⃣ نحول كل Employee إلى EmployeeResponseDto
        List<EmployeeResponseDto> content = page.getContent()
                .stream()
                .map(EmployeeMapper::toDto) // هذا لازم يكون عندك EmployeeMapper
                .toList();

        // 8️⃣ نرجع PagedResponse جاهز للـ API
        return PagedResponse.from(page, content);
    }




    public Employee updateProfile(Long id, String name, String email) {
        Employee employee = findById(id); // reuse your existing findById
        employee.setName(name);
        employee.setEmail(email);
        return repository.save(employee);
    }




}


