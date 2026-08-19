package com.sartiniomar.library.catalog.application.port.in.bookInstance;

import java.util.UUID;

public record CreateBookInstanceCommand(UUID bookId) {
}
