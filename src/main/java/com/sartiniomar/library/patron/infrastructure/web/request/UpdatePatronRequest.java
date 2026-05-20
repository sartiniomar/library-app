package com.sartiniomar.library.patron.infrastructure.web.request;

import com.sartiniomar.library.patron.domain.patron.PatronType;

public record UpdatePatronRequest(
    PatronType type,
    String name,
    String email
) {
}
