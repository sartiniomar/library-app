package com.sartiniomar.library.lending.application.port.in;

import com.sartiniomar.library.lending.domain.book.BookType;
import java.util.UUID;

public record UpdateBookInstanceCommand(UUID id, UUID bookId, BookType type, Boolean onHold) {
}
