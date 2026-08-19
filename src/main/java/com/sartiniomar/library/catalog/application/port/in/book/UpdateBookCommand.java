package com.sartiniomar.library.catalog.application.port.in.book;

import java.util.UUID;

public record UpdateBookCommand(
    UUID id,
    String title,
    String author,
    String isbn
) {}
