package com.sartiniomar.library.lending.infrastructure.web;

import com.sartiniomar.library.lending.application.port.in.CreateBookInstanceCommand;
import com.sartiniomar.library.lending.application.port.in.CreateCirculatingBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.in.CreateRestrictedBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.in.DeleteBookInstanceUseCase;
import com.sartiniomar.library.lending.application.port.in.GetAllBookInstancesByBookIdUseCase;
import com.sartiniomar.library.lending.application.port.in.GetBookInstanceByIdUseCase;
import com.sartiniomar.library.lending.application.port.in.UpdateBookInstanceCommand;
import com.sartiniomar.library.lending.application.port.in.UpdateBookInstanceUseCase;
import com.sartiniomar.library.lending.infrastructure.mapper.BookInstanceMapper;
import com.sartiniomar.library.lending.infrastructure.web.dto.BookInstanceResponse;
import com.sartiniomar.library.lending.infrastructure.web.dto.UpdateBookInstanceRequest;
import lombok.AllArgsConstructor;
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
    CreateBookInstanceCommand command = new CreateBookInstanceCommand(bookId);
    return ResponseEntity.ok(bookInstanceMapper.bookInstanceToBookInstanceResponse(createCirculatingBookInstanceService.execute(command)));
  }

  @PostMapping("/restricted")
  public ResponseEntity<BookInstanceResponse> createRestricted(@PathVariable UUID bookId) {
    CreateBookInstanceCommand command = new CreateBookInstanceCommand(bookId);
    return ResponseEntity.ok(bookInstanceMapper.bookInstanceToBookInstanceResponse(createRestrictedBookInstanceService.execute(command)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BookInstanceResponse> update(@PathVariable UUID bookId, @PathVariable UUID id, @RequestBody UpdateBookInstanceRequest request) {
    UpdateBookInstanceCommand command = new UpdateBookInstanceCommand(id, request.type(), request.onHold());
    return ResponseEntity.ok(bookInstanceMapper.bookInstanceToBookInstanceResponse(updateBookInstanceService.execute(command)));
  }

  @GetMapping
  public ResponseEntity<List<BookInstanceResponse>> listByBookId(@PathVariable UUID bookId) {
    return ResponseEntity.ok(getAllBookInstancesByBookIdService.execute(bookId).stream().map(bookInstanceMapper::bookInstanceToBookInstanceResponse).collect(java.util.stream.Collectors.toList()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookInstanceResponse> get(@PathVariable UUID bookId, @PathVariable UUID id) {
    return ResponseEntity.ok(bookInstanceMapper.bookInstanceToBookInstanceResponse(getBookInstanceByIdService.execute(id)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID bookId, @PathVariable UUID id) {
    deleteBookInstanceService.execute(id);
    return ResponseEntity.noContent().build();
  }
}
