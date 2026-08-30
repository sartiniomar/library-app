package com.sartiniomar.library.loan.application.port.in.reserve;

import java.util.UUID;

public record ReserveCommand(UUID patronId, UUID bookInstanceId) {
}
