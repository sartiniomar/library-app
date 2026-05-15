package com.sartiniomar.library.lending.infrastructure.persistence.jpa.model;

import com.sartiniomar.library.lending.model.book.BookType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "book_instance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInstanceEntity {

  @Id
  private UUID id;

  private String bookId;

  @Enumerated(EnumType.STRING)
  private BookType type;

  private Boolean onHold;

  @Version
  private Long version;

  public BookInstanceEntity(UUID id, String bookId, BookType type, boolean onHold) {
    this.id = id;
    this.bookId = bookId;
    this.type = type;
    this.onHold = onHold;
  }

  public void setOnHold(Boolean onHold) {
    this.onHold = onHold;
  }

  public void setType(BookType bookType) {
    this.type = bookType;
  }

  public Boolean isOnHold() {
    return this.onHold;
  }
}