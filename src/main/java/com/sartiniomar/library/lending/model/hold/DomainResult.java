package com.sartiniomar.library.lending.model.hold;

import java.util.List;

public record DomainResult<T>(T result, List<Object> events) {

}
