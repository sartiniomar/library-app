package com.sartiniomar.library.loan.domain.loan;

import java.util.List;

public record DomainResult<T>(T result, List<Object> events) {

}
