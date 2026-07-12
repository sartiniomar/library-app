package com.sartiniomar.library.lending.infrastructure.web.dto;

import com.sartiniomar.library.lending.domain.book.BookType;
import java.util.UUID;

public record BookInstanceResponse(UUID id,
                                   UUID bookId,
                                   BookType type,
                                   Boolean onHold) {
}
