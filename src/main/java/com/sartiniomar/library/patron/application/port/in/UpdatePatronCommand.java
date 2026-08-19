package com.sartiniomar.library.patron.application.port.in;

import com.sartiniomar.library.patron.domain.patron.PatronType;
import java.util.UUID;

public record UpdatePatronCommand(
    UUID id,
    PatronType type,
    String name,
    String email
) {
}
