package com.sartiniomar.library.catalog.application.port.in.bookInstance;

import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceStatus;

import java.util.UUID;

public record UpdateBookInstanceCommand(UUID id, BookType type, BookInstanceStatus status, Boolean onHold) {
}
