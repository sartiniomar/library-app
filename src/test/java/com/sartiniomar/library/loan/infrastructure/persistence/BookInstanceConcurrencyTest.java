package com.sartiniomar.library.loan.infrastructure.persistence;

import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.infrastructure.persistence.jpa.adapter.BookAdapterRepository;
import com.sartiniomar.library.loan.application.port.in.LoanCommand;
import com.sartiniomar.library.loan.application.usecase.ReserveUseCaseImpl;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.loan.exception.ConcurrentLoanException;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter.LoanBookInstanceAdapterRepository;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter.LoanPatronAdapterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class BookInstanceConcurrencyTest {

  @Autowired
  private BookAdapterRepository bookRepository;

  @Autowired
  private LoanPatronAdapterRepository patronRepository;

  @Autowired
  private LoanBookInstanceAdapterRepository bookInstanceRepository;

  @Autowired
  private ReserveUseCaseImpl reserveUseCase;

  @Test
  void shouldAllowOnlyOneConcurrentReservation() throws Exception {
    // Given
    Book book = Book.create("Book Name", "Author", "12345");
    bookRepository.save(book);

    Patron patron = Patron.regular("Name", "email@email.com");
    patronRepository.save(patron);

    BookInstance bookInstance = BookInstance.circulating(book.getId());
    bookInstanceRepository.save(bookInstance);

    ExecutorService executor = Executors.newFixedThreadPool(2);
    CyclicBarrier barrier = new CyclicBarrier(2);

    Callable<Boolean> reservation = () -> {

      barrier.await();
      LoanCommand command = new LoanCommand(patron.getId(), bookInstance.getId());

      try {
        reserveUseCase.execute(command);
        return true;
      } catch (ConcurrentLoanException e) {
        return false;
      }
    };

    Future<Boolean> resultA = executor.submit(reservation);
    Future<Boolean> resultB = executor.submit(reservation);
    boolean successA = resultA.get();
    boolean successB = resultB.get();
    executor.shutdown();

    assertThat(successA ^ successB).isTrue();

    BookInstance result = bookInstanceRepository.findById(bookInstance.getId()).orElseThrow();

    assertThat(result.getStatus()).isEqualTo(BookInstanceStatus.RESERVED);
  }
}
