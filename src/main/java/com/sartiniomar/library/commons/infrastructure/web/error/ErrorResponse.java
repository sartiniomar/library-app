package com.sartiniomar.library.commons.infrastructure.web.error;

import java.util.List;

public record ErrorResponse(
    String code,
    List<Error> errors
) {}
