package com.sartiniomar.library.loan.integration.support.factory;

import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.support.builder.LoanTestDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class BookInstanceLoanTestFactory {

  @Autowired
  private BookInstanceLoanRepository bookInstanceLoanRepository;

  public BookInstance createDefaultBookInstance(
      BookType bookType,
      BookInstanceStatus bookInstanceStatus)
  {
    return bookInstanceLoanRepository.save(
        new LoanTestDataBuilder().buildDefaultBookInstance(bookType, bookInstanceStatus));
  }

  public BookInstance createBookInstance(
      UUID bookInstanceId,
      UUID bookId,
      BookType bookType,
      BookInstanceStatus bookInstanceStatus)
  {
    return bookInstanceLoanRepository.save(
        new LoanTestDataBuilder().buildBookInstance(bookInstanceId, bookId, bookType, bookInstanceStatus));
  }
}
