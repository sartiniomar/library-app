package com.sartiniomar.library.catalog.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(
    @NotBlank(message = "title is required")
    String title,
    @NotBlank(message = "author is required")
    String author,
    @NotBlank(message = "isbn is required")
    String isbn
) {
}
