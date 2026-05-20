package com.sartiniomar.library.patron.infrastructure.web.request;

import jakarta.validation.constraints.NotBlank;

public record CreatePatronRequest(
    @NotBlank(message = "name is required")
    String name,
    @NotBlank(message = "email is required")
    String email
) {
}
