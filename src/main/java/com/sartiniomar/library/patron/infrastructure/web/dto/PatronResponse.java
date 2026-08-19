package com.sartiniomar.library.patron.infrastructure.web.dto;

import com.sartiniomar.library.patron.domain.patron.PatronType;
import java.util.UUID;

public record PatronResponse(UUID id,
    PatronType type,
    String name,
    String email) {
}
