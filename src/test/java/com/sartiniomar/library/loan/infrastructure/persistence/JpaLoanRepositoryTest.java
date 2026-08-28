package com.sartiniomar.library.loan.infrastructure.persistence;

import com.sartiniomar.library.loan.domain.loan.DomainPolicy;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter.LoanAdapterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(LoanAdapterRepository.class)
public class JpaLoanRepositoryTest {

  @Autowired
  private LoanAdapterRepository repository;

  @Test
  void shouldSaveAndFindLoan() {
    UUID bookInstanceId = java.util.UUID.randomUUID();
    UUID patronId = java.util.UUID.randomUUID();

    Loan hold = Loan.createReserve(patronId, bookInstanceId, Clock.systemDefaultZone());

    repository.save(hold);

    Optional<Loan> result = repository.findById(hold.getId());

    assertTrue(result.isPresent());
    assertEquals(patronId, result.get().getPatronId());
    assertEquals(bookInstanceId, result.get().getBookInstanceId());
  }

  @Test
  void shouldCountLoansByPatronId() {
    UUID patronId = java.util.UUID.randomUUID();

    Loan hold1 = Loan.createReserve(patronId, java.util.UUID.randomUUID(), Clock.systemDefaultZone());
    Loan hold2 = Loan.createReserve(patronId, java.util.UUID.randomUUID(), Clock.systemDefaultZone());

    repository.save(hold1);
    repository.save(hold2);

    int count = repository.countActiveLoansByPatronId(patronId, DomainPolicy.ACTIVE_STATUSES);

    assertEquals(2, count);
  }
}