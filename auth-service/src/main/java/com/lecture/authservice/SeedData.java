package com.lecture.authservice;

import com.lecture.authservice.entities.AppUser;
import com.lecture.authservice.entities.Role;
import com.lecture.authservice.repositories.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class SeedData {

    private static final Logger log = LoggerFactory.getLogger(SeedData.class);

    @Bean
    CommandLineRunner initDatabase(AppUserRepository repo, PasswordEncoder encoder) {

        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                repo.save(new AppUser(null, "admin", encoder.encode("Admin@123"), true, Set.of(Role.ADMIN), null));
            }
            if (repo.findByUsername("hr").isEmpty()) {
                repo.save(new AppUser(null, "hr", encoder.encode("Hr@123"), true, Set.of(Role.HR), null));
            }
        };
    }
}
