package com.sartiniomar.library.lending.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PlaceHoldRequest(
    @NotNull(message = "bookId is required")
    UUID bookId,
    @NotNull(message = "patronId is required")
    UUID patronId
) {
  public UUID getPatronId() {
    return this.patronId;
  }

  public UUID getBookId() {
    return this.bookId;
  }
}
