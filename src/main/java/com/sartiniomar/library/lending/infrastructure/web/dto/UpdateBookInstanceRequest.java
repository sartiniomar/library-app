package com.sartiniomar.library.lending.infrastructure.web.dto;

import com.sartiniomar.library.lending.domain.book.BookType;

public record UpdateBookInstanceRequest(
    BookType type,
    Boolean onHold
) {
}
