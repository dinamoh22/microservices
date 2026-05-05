package org.exampl;



import java.util.List;

public interface DepartmentService {


    List<Department> findAll();
    Department findById(Long id);
    Department create(Department department);
    //void deleteByIdOrThrow(Long id);
    Department updateExisting(Long id, DepartmentRequestDto dto);
    //List<Employee> findEmployeesByDepartment(Long deptId);

}
