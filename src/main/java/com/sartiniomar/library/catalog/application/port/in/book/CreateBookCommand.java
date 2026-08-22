package com.sartiniomar.library.catalog.application.port.in.book;

public record CreateBookCommand(
    String title,
    String author,
    String isbn
) {}
