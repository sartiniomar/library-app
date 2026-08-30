package com.sartiniomar.library.loan.application.port.in.cancel;

import java.util.UUID;

public record CancelCommand(UUID loanId) {
}
