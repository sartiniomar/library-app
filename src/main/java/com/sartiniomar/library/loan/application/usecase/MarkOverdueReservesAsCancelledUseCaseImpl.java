package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.MarkOverdueReservesAsCancelledUseCase;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.loan.domain.loan.Loan;
import java.util.List;

public class MarkOverdueReservesAsCancelledUseCaseImpl implements MarkOverdueReservesAsCancelledUseCase {

  private final LoanRepository loanRepository;

  private final BookInstanceLoanRepository bookInstanceLoanRepository;

  public MarkOverdueReservesAsCancelledUseCaseImpl(LoanRepository loanRepository, BookInstanceLoanRepository bookInstanceLoanRepository) {
    this.loanRepository = loanRepository;
    this.bookInstanceLoanRepository = bookInstanceLoanRepository;
  }

  @Override
  public void execute() {
    List<Loan> loans = loanRepository.findReservesDue();
    for (Loan loan : loans) {
      BookInstance bookInstance = bookInstanceLoanRepository.findById(loan.getBookInstanceId())
          .orElseThrow(() -> new BookInstanceNotFoundException(loan.getBookInstanceId().toString()));
      loan.cancelled();
      bookInstance.available();
      loanRepository.save(loan);
      bookInstanceLoanRepository.save(bookInstance);
    }
  }
}
