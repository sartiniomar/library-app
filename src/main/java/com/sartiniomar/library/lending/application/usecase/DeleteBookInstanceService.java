package com.sartiniomar.library.lending.application.usecase;

import com.sartiniomar.library.lending.application.port.in.DeleteBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import com.sartiniomar.library.lending.domain.book.BookInstanceNotFoundException;
import java.util.UUID;

public class DeleteBookInstanceService implements DeleteBookInstanceUseCase {

  private final BookInstanceRepository bookInstanceRepository;

  public DeleteBookInstanceService(BookInstanceRepository bookInstanceRepository) {
    this.bookInstanceRepository = bookInstanceRepository;
  }

  @Override
  public void execute(UUID id) {
    BookInstance existingBookInstance = bookInstanceRepository.findById(id)
        .orElseThrow(() -> new BookInstanceNotFoundException("UUID=" + id.toString()));
    bookInstanceRepository.delete(existingBookInstance.getId());
  }
}
