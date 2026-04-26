package com.sartiniomar.library.lending.infrastructure.web.response;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    String code,
    Map<String, String> errors,
    Instant timestamp
) {}
