package com.sartiniomar.library.loan.infrastructure.persistence;

import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookMapperImpl;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter.BookAdapterRepository;
import com.sartiniomar.library.catalog.support.builder.BookTestDataBuilder;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter.LoanAdapterRepository;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter.LoanBookInstanceAdapterRepository;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter.LoanPatronAdapterRepository;
import com.sartiniomar.library.loan.support.builder.BookInstanceLoanTestDataBuilder;
import com.sartiniomar.library.loan.support.builder.LoanTestDataBuilder;
import com.sartiniomar.library.loan.support.builder.PatronLoanTestDataBuilder;
import com.sartiniomar.library.loan.domain.patron.Patron;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.time.Clock;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({
    LoanAdapterRepository.class,
    BookAdapterRepository.class,
    LoanPatronAdapterRepository.class,
    LoanBookInstanceAdapterRepository.class,
    BookMapperImpl.class
})
public class JpaLoanRepositoryTest {

  @Autowired
  private LoanAdapterRepository loanRepository;

  @Autowired
  private BookAdapterRepository bookRepository;

  @Autowired
  private LoanPatronAdapterRepository patronRepository;

  @Autowired
  private LoanBookInstanceAdapterRepository bookInstanceRepository;

  @Test
  void shouldSaveAndFindLoan() {
    Patron patron = new PatronLoanTestDataBuilder().buildDefaultRegular();
    patronRepository.save(patron);

    Book book = new BookTestDataBuilder().buildDefault();
    bookRepository.save(book);

    BookInstance bookInstance = new BookInstanceLoanTestDataBuilder().buildCirculatingDefault(book.getId());
    bookInstanceRepository.save(bookInstance);

    Loan loan = new LoanTestDataBuilder().buildReserve(patron, bookInstance);
    loanRepository.save(loan);

    Optional<Loan> result = loanRepository.findById(loan.getId());

    assertTrue(result.isPresent());
    assertEquals(patron.getId(), result.get().getPatronId());
    assertEquals(bookInstance.getId(), result.get().getBookInstanceId());
  }

  @Test
  void shouldCountLoansByPatronId() {
    Patron patron = new PatronLoanTestDataBuilder().buildDefaultRegular();
    patronRepository.save(patron);

    Book book = new BookTestDataBuilder().buildDefault();
    bookRepository.save(book);

    BookInstance bookInstance1 = new BookInstanceLoanTestDataBuilder().buildCirculatingDefault(book.getId());
    BookInstance bookInstance2 = new BookInstanceLoanTestDataBuilder().buildCirculatingDefault(book.getId());
    bookInstanceRepository.save(bookInstance1);
    bookInstanceRepository.save(bookInstance2);

    Loan loan1 = Loan.createReserve(patron.getId(), bookInstance1.getId(), Clock.systemUTC());
    Loan loan2 = Loan.createReserve(patron.getId(), bookInstance2.getId(), Clock.systemUTC());

    loanRepository.save(loan1);
    loanRepository.save(loan2);

    long count = loanRepository.countActiveLoansByPatronId(patron.getId(), Loan.ACTIVE_STATUSES);

    assertEquals(2, count);
  }
}