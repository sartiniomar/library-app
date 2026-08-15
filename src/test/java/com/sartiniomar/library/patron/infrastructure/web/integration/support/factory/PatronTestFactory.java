package com.sartiniomar.library.patron.infrastructure.web.integration.support.factory;

import com.sartiniomar.library.patron.application.port.out.PatronRepository;
import com.sartiniomar.library.patron.domain.patron.Patron;
import com.sartiniomar.library.patron.domain.patron.PatronType;
import com.sartiniomar.library.patron.support.builder.PatronTestDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class PatronTestFactory {

  @Autowired
  private PatronRepository patronRepository;

  public Patron createDefaultRegular() {
    return patronRepository.save(new PatronTestDataBuilder().buildDefaultRegular());
  }

  public Patron createDefaultResearcher() {
    return patronRepository.save(new PatronTestDataBuilder().buildDefaultResearcher());
  }

  public Patron create(String name, String email, PatronType type) {
    return patronRepository.save(new PatronTestDataBuilder().build(UUID.randomUUID(), type, name, email));
  }
}
