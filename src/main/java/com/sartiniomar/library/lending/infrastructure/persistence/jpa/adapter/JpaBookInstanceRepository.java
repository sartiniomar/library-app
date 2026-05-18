package com.sartiniomar.library.lending.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.lending.application.port.out.BookInstanceRepository;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.mapper.BookInstanceMapper;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.model.BookInstanceEntity;
import com.sartiniomar.library.lending.infrastructure.persistence.jpa.repository.SpringDataBookInstanceRepository;
import com.sartiniomar.library.lending.domain.book.BookInstance;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaBookInstanceRepository implements BookInstanceRepository {

  private final SpringDataBookInstanceRepository jpaRepo;

  public JpaBookInstanceRepository(SpringDataBookInstanceRepository jpaRepo) {
    this.jpaRepo = jpaRepo;
  }

  @Override
  public Optional<BookInstance> findById(UUID id) {
    return jpaRepo.findById(id)
        .map(BookInstanceMapper::toDomain);
  }

  @Override
  @Transactional
  public void save(BookInstance domain) {

    BookInstanceEntity entity = jpaRepo.findById(domain.getId())
        .orElse(null);

    if (entity == null) {
      // INSERT
      jpaRepo.save(BookInstanceMapper.toEntity(domain));
    } else {
      // UPDATE dentro del mismo persistence context
      BookInstanceMapper.updateEntity(entity, domain);
      // NO LLAMAR save() acá -> evita duplicación de instancia
    }
  }
}