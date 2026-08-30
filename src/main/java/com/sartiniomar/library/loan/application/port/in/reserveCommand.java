package com.sartiniomar.library.loan.application.port.in;

import java.util.UUID;

public record reserveCommand(UUID patronId, UUID bookInstanceId) {
}
