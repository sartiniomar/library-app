package com.sartiniomar.library.holding.model.hold;

import java.util.List;

public record DomainResult<T>(T result, List<Object> events) {

}
