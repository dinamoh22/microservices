package org.exampl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AuthSeeder {

    @Bean
    CommandLineRunner seedUsers(AppUserRepository userRepo, PasswordEncoder encoder) {
        return args -> {

            if (userRepo.findByUsername("admin").isEmpty()) {
                AppUser admin = new AppUser(
                        "admin",
                        encoder.encode("Admin@123"),
                        true,
                        Set.of(Role.ADMIN)
                );
                userRepo.save(admin);
                System.out.println("[AuthSeeder] admin user seeded.");
            } else {
                System.out.println("[AuthSeeder] admin already exists, skipping.");
            }

            if (userRepo.findByUsername("hr").isEmpty()) {
                AppUser hr = new AppUser(
                        "hr",
                        encoder.encode("Hr@123"),
                        true,
                        Set.of(Role.HR)
                );
                userRepo.save(hr);
                System.out.println("[AuthSeeder] hr user seeded.");
            } else {
                System.out.println("[AuthSeeder] hr already exists, skipping.");
            }

            if (userRepo.findByUsername("employee1").isEmpty()) {
                AppUser emp1 = new AppUser(
                        "employee1",
                        encoder.encode("Emp@123"),
                        true,
                        Set.of(Role.EMPLOYEE)
                );
                emp1.setEmployeeId(1L);
                userRepo.save(emp1);
                System.out.println("[AuthSeeder] employee1 user seeded.");
            } else {
                System.out.println("[AuthSeeder] employee1 already exists, skipping.");
            }

            if (userRepo.findByUsername("employee2").isEmpty()) {
                AppUser emp2 = new AppUser(
                        "employee2",
                        encoder.encode("Emp@123"),
                        true,
                        Set.of(Role.EMPLOYEE)
                );
                emp2.setEmployeeId(2L);
                userRepo.save(emp2);
                System.out.println("[AuthSeeder] employee2 user seeded.");
            } else {
                System.out.println("[AuthSeeder] employee2 already exists, skipping.");
            }

            System.out.println("[AuthSeeder] Done. Credentials:");
            System.out.println("  admin    / Admin@123  (ADMIN)");
            System.out.println("  hr       / Hr@123     (HR)");
            System.out.println("  employee1/ Emp@123    (EMPLOYEE, empId=1)");
            System.out.println("  employee2/ Emp@123    (EMPLOYEE, empId=2)");
        };
    }
}
