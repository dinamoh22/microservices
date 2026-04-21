package com.lecture.authservice.dtos;

import com.lecture.authservice.entities.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotNull Long employeeId,
        @NotEmpty Set<Role> roles
) {}
