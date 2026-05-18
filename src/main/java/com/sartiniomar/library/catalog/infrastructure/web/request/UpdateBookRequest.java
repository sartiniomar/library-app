package com.sartiniomar.library.catalog.infrastructure.web.request;

public record UpdateBookRequest(
    String title,
    String author,
    String isbn
) {

}
