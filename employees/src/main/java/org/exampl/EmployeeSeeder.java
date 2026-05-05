package org.exampl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class EmployeeSeeder {

    @Bean
    CommandLineRunner seedEmployees(EmployeeRepository repo) {
        return args -> {
            if (repo.count() == 0) {

                // Department IDs must match what DepartmentSeeder creates:
                // 1 = Engineering, 2 = Human Resources, 3 = Finance, 4 = Marketing, 5 = Operations

                Employee e1 = new Employee("Alice Johnson", "Software Engineer");
                e1.setEmail("alice@company.com");
                e1.setSalary(85000);
                e1.setYearsOfExperience(5);
                e1.setHiredDate(LocalDate.of(2020, 3, 15));
                e1.setDepartmentId(1L);

                Employee e2 = new Employee("Bob Smith", "Senior Developer");
                e2.setEmail("bob@company.com");
                e2.setSalary(95000);
                e2.setYearsOfExperience(8);
                e2.setHiredDate(LocalDate.of(2018, 7, 1));
                e2.setDepartmentId(1L);

                Employee e3 = new Employee("Carol White", "HR Manager");
                e3.setEmail("carol@company.com");
                e3.setSalary(72000);
                e3.setYearsOfExperience(6);
                e3.setHiredDate(LocalDate.of(2019, 1, 20));
                e3.setDepartmentId(2L);

                Employee e4 = new Employee("David Brown", "Financial Analyst");
                e4.setEmail("david@company.com");
                e4.setSalary(78000);
                e4.setYearsOfExperience(4);
                e4.setHiredDate(LocalDate.of(2021, 5, 10));
                e4.setDepartmentId(3L);

                Employee e5 = new Employee("Eva Martinez", "Marketing Lead");
                e5.setEmail("eva@company.com");
                e5.setSalary(80000);
                e5.setYearsOfExperience(7);
                e5.setHiredDate(LocalDate.of(2017, 9, 5));
                e5.setDepartmentId(4L);

                repo.save(e1);
                repo.save(e2);
                repo.save(e3);
                repo.save(e4);
                repo.save(e5);

                System.out.println("[EmployeeSeeder] 5 employees seeded.");
            } else {
                System.out.println("[EmployeeSeeder] Employees already exist, skipping.");
            }
        };
    }
}
