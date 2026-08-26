package com.sartiniomar.library.catalog.infrastructure.web.dto;

import com.sartiniomar.library.catalog.domain.bookInstance.BookType;

public record UpdateBookInstanceRequest(
    BookType type
) {
}
