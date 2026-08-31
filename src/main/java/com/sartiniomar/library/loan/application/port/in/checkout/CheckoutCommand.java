package com.sartiniomar.library.loan.application.port.in.checkout;

import java.util.UUID;

public record CheckoutCommand(UUID patronId, UUID bookInstanceId) {
}
