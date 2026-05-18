package com.sartiniomar.library.catalog.application.port.in;

public record CreateBookCommand(
    String title,
    String author,
    String isbn
) {}
