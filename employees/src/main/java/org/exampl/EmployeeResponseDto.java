package org.exampl;

import java.time.LocalDate;

public class EmployeeResponseDto {

    private Long id;
    private String name;
    private String role;
    private String email;
    private Integer yearsOfExperience;
    private LocalDate hiredDate;
    private Double salary;
    private String departmentName;



    public EmployeeResponseDto(Long id, String name, String role, String email, Integer yearsOfExperience, LocalDate hiredDate, Double salary) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.yearsOfExperience = yearsOfExperience;
        this.hiredDate = hiredDate;
        this.salary = salary;
    }

    public EmployeeResponseDto(Long id, String name, String role, String email, Integer yearsOfExperience, LocalDate hiredDate, Double salary,String departmentName) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.yearsOfExperience = yearsOfExperience;
        this.hiredDate = hiredDate;
        this.salary = salary;
        this.departmentName = departmentName;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public LocalDate getHiredDate() { return hiredDate; }
    public Double getSalary() { return salary; }
    public String getDepartmentName() { return departmentName; }

}
