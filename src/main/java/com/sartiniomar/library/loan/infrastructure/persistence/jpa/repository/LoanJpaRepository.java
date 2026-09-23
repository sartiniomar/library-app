package com.sartiniomar.library.loan.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.infrastructure.persistence.model.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface LoanJpaRepository extends JpaRepository<LoanEntity, UUID> {

  @Query("""
        SELECT COUNT(l)
        FROM LoanEntity l
        WHERE l.patron.id = :patronId
          AND l.status IN :statuses
      """)
  Long countActiveLoansByPatronId(
      @Param("patronId") UUID patronId,
      @Param("statuses") List<LoanStatus> statuses
  );

  List<LoanEntity> findAllByPatron_Id(UUID patronId);

  @Query("""
    SELECT l
    FROM LoanEntity l
    WHERE l.status = :status
      AND l.dueAt < :now
    """)
  List<LoanEntity> findLoansDue(
      @Param("status") LoanStatus status,
      @Param("now") Instant now
  );
}
