package com.sartiniomar.library.catalog.infrastructure.web.dto;

import java.util.UUID;

public record BookResponse(UUID id,
    String title,
    String author,
    String isbn) {
}
