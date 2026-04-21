package com.lecture.departmentsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedData {

    private static final Logger log = LoggerFactory.getLogger(SeedData.class);

    @Bean
    CommandLineRunner initDatabase(DepartmentRepository departmentRepository) {
        return args -> {
                Department engineering = departmentRepository.findByName("Engineering")
                        .orElseGet(() -> departmentRepository.save(new Department(null, "Engineering")));
                Department hr = departmentRepository.findByName("HR")
                        .orElseGet(() -> departmentRepository.save(new Department(null, "HR")));
                Department sales = departmentRepository.findByName("Sales")
                        .orElseGet(() -> departmentRepository.save(new Department(null, "Sales")));
                 Department support = departmentRepository.findByName("Support")
                        .orElseGet(() -> departmentRepository.save(new Department(null, "Support")));
                 Department rnd = departmentRepository.findByName("R&D")
                        .orElseGet(() -> departmentRepository.save(new Department(null, "R&D")));
        };

    }
}
