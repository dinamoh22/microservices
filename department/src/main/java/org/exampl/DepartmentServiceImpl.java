package org.exampl;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class DepartmentServiceImpl  implements DepartmentService {

    private final DepartmentRepository repository;
   // private final EmployeeRepository employeeRepository;

    public DepartmentServiceImpl(DepartmentRepository repository) {
        this.repository = repository;
       // this.employeeRepository = employeeRepository;
    }
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Department> findAll() {
        return repository.findAll();
    }

//    public Department create(Department department) {
//        // Optional: ensure client doesn't control ID
//        // employee.setId(null);
//        return repository.save(department);
//    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Department create(Department department) {
        // 1. Check if name already exists (Task E1) [cite: 77, 84]
        if (repository.existsByNameIgnoreCase(department.getName())) {
            // Preferred: 409 Conflict
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department name already exists");
        }

        // 2. Ensure we are creating a new record [cite: 5]
        //department.setId(null);

        return repository.save(department);
    }


//    public Department create(DepartmentRequestDto dto) {
//        if (repository.(dto.getName())) {
//            throw new IllegalStateException("Department already exists");
//        }
//        return repository.save(DepartmentMapper.toEntity(dto));
//    }

//    public Department create(DepartmentRequestDto dto) {
//        if (repository.findByName(dto.getName()).isPresent()) {
//            throw new IllegalStateException("Department already exists");
//        }
//        return repository.save(DepartmentMapper.toEntity(dto));
//    }



    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Department findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

//    @Transactional(readOnly = true)
//    public Department findBylocation(String location) {
//        return repository.findByLocation(location)
//                .orElseThrow(() -> new DepartmentNotFoundException(location));
//    }




//    public void deleteByIdOrThrow(Long id) {
//        if (!repository.existsById(id)) {
//            throw new DepartmentNotFoundException(id);
//        }
//        repository.deleteById(id);
//    }

//    @Override
//    public void deleteByIdOrThrow(Long id) {
//        // 1. Check if it exists (for the 404 requirement)
//        Department department = repository.findById(id)
//                .orElseThrow(() -> new DepartmentNotFoundException(id));
//
//        // 2. Task E2: Check if department has employees before deleting
//        if (!department.getEmployees().isEmpty()) {
//            // This should return a 409 Conflict as per Part E instructions
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "Cannot delete department because it still has employees assigned to it."
//            );
//        }
//
//        repository.delete(department);
//    }
//
//    @Override
//    public Department updateExisting(Long id, DepartmentRequestDto dto) {
//        Department department = findById(id);
//        department.setName(dto.getName());
//        return repository.save(department);
//    }
@Override
@Transactional
@PreAuthorize("hasRole('ADMIN')")
public Department updateExisting(Long id, DepartmentRequestDto dto) {
    // 1. Find the one currently in the DB
    Department department = repository.findById(id)
            .orElseThrow(() -> new DepartmentNotFoundException(id));

    // 2. Set the NEW values from the DTO
    department.setName(dto.getName());
    department.setLocation(dto.getLocation()); // Ensure this setter exists!

    // 3. IMPORTANT: Save it back to the DB
    return repository.save(department);
}


//    @Override
//    @Transactional(readOnly = true)
//    public List<Employee> findEmployeesByDepartment(Long deptId) {
//        if (!repository.existsById(deptId)) {
//            throw new DepartmentNotFoundException(deptId);
//        }
//        return employeeRepository.findByDepartmentId(deptId);
//    }

}
