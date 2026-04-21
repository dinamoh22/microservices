package com.lecture.departmentsservice;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    List<Long> employeesIds;

    public Department(){
        employeesIds = new ArrayList<>();
    }

    public Department(Long id, String name) {
        this.id = id;
        this.name = name;
        this.employeesIds = new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getEmployees() {
        return employeesIds;
    }

    public void addEmployee(Long employee){
        employeesIds.add(employee);
    }
}
