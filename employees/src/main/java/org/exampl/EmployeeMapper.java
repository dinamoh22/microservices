package org.exampl;

public class EmployeeMapper {

    private EmployeeMapper() {}

    public static Employee toEntity(EmployeeRequestDto dto) {
        Employee e = new Employee();
        e.setName(dto.getName());
        e.setRole(dto.getRole());
        e.setEmail(dto.getEmail());
        e.setYearsOfExperience(dto.getYearsOfExperience());
        e.setHiredDate(dto.getHiredDate());
        e.setSalary(dto.getSalary());
        return e;
    }








    public static EmployeeResponseDto toDto(Employee e) {
        return new EmployeeResponseDto(
                e.getId(),
                e.getName(),
                e.getRole(),
                e.getEmail(),
                e.getYearsOfExperience(),
                e.getHiredDate(),
                e.getSalary()
        );
    }



    public static EmployeeResponseDto toDto(Employee e, String departmentName) {
        return new EmployeeResponseDto(
                e.getId(),
                e.getName(),
                e.getRole(),
                e.getEmail(),
                e.getYearsOfExperience(),
                e.getHiredDate(),
                e.getSalary(),
                departmentName // جديد
        );
    }


}
