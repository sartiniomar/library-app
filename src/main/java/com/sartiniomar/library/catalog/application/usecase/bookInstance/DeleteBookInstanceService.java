package com.sartiniomar.library.catalog.application.usecase.bookInstance;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.DeleteBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceNotFoundException;
import java.util.UUID;

public class DeleteBookInstanceService implements DeleteBookInstanceUseCase {

  private final BookInstanceRepository bookInstanceRepository;

  public DeleteBookInstanceService(BookInstanceRepository bookInstanceRepository) {
    this.bookInstanceRepository = bookInstanceRepository;
  }

  @Override
  public void execute(UUID id) {
    BookInstance existingBookInstance = bookInstanceRepository.findById(id)
        .orElseThrow(() -> new BookInstanceNotFoundException("Book Instance not found with id: " + id.toString()));
    bookInstanceRepository.delete(existingBookInstance.getId());
  }
}
