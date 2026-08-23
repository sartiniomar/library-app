package com.sartiniomar.library.catalog.infrastructure.web.dto;

import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import java.util.UUID;

public record BookInstanceResponse(UUID id,
                                   UUID bookId,
                                   BookType type,
                                   Boolean onLoan) {
}
