package com.sartiniomar.library.loan.infrastructure.web.dto;

import java.util.UUID;

public record HoldResponse(UUID id,
    UUID patronId,
    UUID bookInstanceId) {
}
