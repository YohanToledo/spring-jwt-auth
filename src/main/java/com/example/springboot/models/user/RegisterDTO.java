package com.example.springboot.models.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(@NotBlank String login, @NotBlank String password, @NotNull UserRole role) {

}
