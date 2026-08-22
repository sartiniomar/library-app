package com.sartiniomar.library.loan.application.port.in;

import java.util.UUID;

public record PlaceHoldCommand(UUID patronId, UUID bookInstanceId) {
}
