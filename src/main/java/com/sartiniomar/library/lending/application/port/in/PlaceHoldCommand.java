package com.sartiniomar.library.lending.application.port.in;

import java.util.UUID;

public record PlaceHoldCommand(UUID patronId, UUID bookInstanceId) {
}
