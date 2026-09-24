package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarkOverdueReservesAsCancelledUseCaseTest {

  @Mock
  private LoanRepository loanRepository;

  @Mock
  private BookInstanceLoanRepository bookInstanceLoanRepository;

  @InjectMocks
  private MarkOverdueReservesAsCancelledUseCaseImpl useCase;

  @Test
  void shouldExecuteMarkOverdueReservesSuccessfully() {
    BookInstance bookInstance1 = BookInstance.circulating(UUID.randomUUID());
    BookInstance bookInstance2 = BookInstance.circulating(UUID.randomUUID());
    Loan loan1 = Loan.createReserve(UUID.randomUUID(), bookInstance1.getId(), Clock.systemUTC());
    Loan loan2 = Loan.createReserve(UUID.randomUUID(), bookInstance2.getId(), Clock.systemUTC());
    bookInstance1.reserved();
    bookInstance2.reserved();

    when(loanRepository.findReservesDue()).thenReturn(List.of(loan1, loan2));
    when(bookInstanceLoanRepository.findById(bookInstance1.getId())).thenReturn(Optional.of(bookInstance1));
    when(bookInstanceLoanRepository.findById(bookInstance2.getId())).thenReturn(Optional.of(bookInstance2));

    when(loanRepository.save(loan1)).thenReturn(loan1);
    when(loanRepository.save(loan2)).thenReturn(loan2);
    when(bookInstanceLoanRepository.save(bookInstance1)).thenReturn(bookInstance1);
    when(bookInstanceLoanRepository.save(bookInstance2)).thenReturn(bookInstance2);

    useCase.execute();

    assertEquals(LoanStatus.CANCELLED, loan1.getStatus());
    assertEquals(LoanStatus.CANCELLED, loan2.getStatus());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance1.getStatus());
    assertEquals(BookInstanceStatus.AVAILABLE, bookInstance2.getStatus());

    verify(loanRepository).findReservesDue();
    verify(bookInstanceLoanRepository).findById(bookInstance1.getId());
    verify(bookInstanceLoanRepository).findById(bookInstance2.getId());
    verify(loanRepository).save(loan1);
    verify(loanRepository).save(loan2);
    verify(bookInstanceLoanRepository).save(bookInstance1);
    verify(bookInstanceLoanRepository).save(bookInstance2);
  }

  @Test
  void should_throw_exception_when_book_instance_not_exist() {
    Loan loan1 = Loan.createReserve(UUID.randomUUID(), UUID.randomUUID(), Clock.systemUTC());
    Loan loan2 = Loan.createReserve(UUID.randomUUID(), UUID.randomUUID(), Clock.systemUTC());

    when(loanRepository.findReservesDue()).thenReturn(List.of(loan1, loan2));

    BookInstanceNotFoundException ex =
        assertThrows(BookInstanceNotFoundException.class,
            () -> useCase.execute()
        );

    assertEquals("Book Instance not found: " + loan1.getBookInstanceId(), ex.getMessage());
    assertEquals(LoanStatus.RESERVED, loan1.getStatus());
    assertEquals(LoanStatus.RESERVED, loan2.getStatus());

    verify(loanRepository).findReservesDue();
    verify(bookInstanceLoanRepository).findById(loan1.getBookInstanceId());
  }
}
