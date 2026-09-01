package com.sartiniomar.library.loan.application.port.in;

import java.util.UUID;

public record LoanCommand(UUID patronId, UUID bookInstanceId) {
}
