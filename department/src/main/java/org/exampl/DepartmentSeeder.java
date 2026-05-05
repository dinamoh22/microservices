package org.exampl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepartmentSeeder {

    @Bean
    CommandLineRunner seedDepartments(DepartmentRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Department("Engineering", "Building A"));
                repo.save(new Department("Human Resources", "Building B"));
                repo.save(new Department("Finance", "Building C"));
                repo.save(new Department("Marketing", "Building D"));
                repo.save(new Department("Operations", "Building E"));
                System.out.println("[DepartmentSeeder] 5 departments seeded.");
            } else {
                System.out.println("[DepartmentSeeder] Departments already exist, skipping.");
            }
        };
    }
}
