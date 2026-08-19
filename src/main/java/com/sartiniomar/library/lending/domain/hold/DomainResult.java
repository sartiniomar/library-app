package com.sartiniomar.library.lending.domain.hold;

import java.util.List;

public record DomainResult<T>(T result, List<Object> events) {

}
