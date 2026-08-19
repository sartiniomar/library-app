package com.sartiniomar.library.lending.infrastructure.web.dto;

import java.util.UUID;

public record HoldResponse(UUID id,
    UUID patronId,
    UUID bookInstanceId) {
}
