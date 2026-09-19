package com.sartiniomar.library.catalog.infrastructure.web;

import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateBookInstanceCommand;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateCirculatingBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateRestrictedBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.DeleteBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.GetAllBookInstancesByBookIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.GetBookInstanceByIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.UpdateBookInstanceCommand;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.UpdateBookInstanceUseCase;
import com.sartiniomar.library.catalog.infrastructure.mapper.BookInstanceMapper;
import com.sartiniomar.library.catalog.infrastructure.web.dto.BookInstanceResponse;
import com.sartiniomar.library.catalog.infrastructure.web.dto.UpdateBookInstanceRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/books/{bookId}/instances")
public class BookInstanceController {

  private final BookInstanceMapper bookInstanceMapper;
  private final CreateCirculatingBookInstanceUseCase createCirculatingBookInstanceService;
  private final CreateRestrictedBookInstanceUseCase createRestrictedBookInstanceService;
  private final UpdateBookInstanceUseCase updateBookInstanceService;
  private final GetAllBookInstancesByBookIdUseCase getAllBookInstancesByBookIdService;
  private final GetBookInstanceByIdUseCase getBookInstanceByIdService;
  private final DeleteBookInstanceUseCase deleteBookInstanceService;

  @PostMapping("/circulating")
  public ResponseEntity<BookInstanceResponse> createCirculating(@PathVariable UUID bookId) {
    log.debug("Creating circulating book instance. Book Id= {}", bookId);
    CreateBookInstanceCommand command = new CreateBookInstanceCommand(bookId);
    return ResponseEntity.ok(bookInstanceMapper.bookInstanceToBookInstanceResponse(createCirculatingBookInstanceService.execute(command)));
  }

  @PostMapping("/restricted")
  public ResponseEntity<BookInstanceResponse> createRestricted(@PathVariable UUID bookId) {
    log.debug("Creating restricted book instance. Book Id= {}", bookId);
    CreateBookInstanceCommand command = new CreateBookInstanceCommand(bookId);
    return ResponseEntity.ok(bookInstanceMapper.bookInstanceToBookInstanceResponse(createRestrictedBookInstanceService.execute(command)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BookInstanceResponse> update(@PathVariable UUID id, @RequestBody UpdateBookInstanceRequest request) {
    log.debug("Updating book instance. Book Instance Id= {}", id);
    UpdateBookInstanceCommand command = new UpdateBookInstanceCommand(id, request.type());
    return ResponseEntity.ok(bookInstanceMapper.bookInstanceToBookInstanceResponse(updateBookInstanceService.execute(command)));
  }

  @GetMapping
  public ResponseEntity<List<BookInstanceResponse>> listByBookId(@PathVariable UUID bookId) {
    log.debug("Getting book instances by book id. Book Id= {}", bookId);
    return ResponseEntity.ok(getAllBookInstancesByBookIdService
        .execute(bookId)
        .stream()
        .map(bookInstanceMapper::bookInstanceToBookInstanceResponse)
        .collect(java.util.stream.Collectors.toList()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookInstanceResponse> get(@PathVariable UUID id) {
    log.debug("Getting book instance by id. Book Instance Id= {}", id);
    return ResponseEntity.ok(bookInstanceMapper.bookInstanceToBookInstanceResponse(getBookInstanceByIdService.execute(id)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    log.debug("Deleting book instance. Book Instance Id= {}", id);
    deleteBookInstanceService.execute(id);
    return ResponseEntity.noContent().build();
  }
}
