package br.com.thaynarbd.budgeting.infrastructure.persistence.repository;

import br.com.thaynarbd.budgeting.domain.model.TransactionType;
import br.com.thaynarbd.budgeting.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpringDataTransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByDateBetweenOrderByDateDesc(LocalDate from, LocalDate to);
    List<TransactionEntity> findByDateBetweenAndTypeOrderByDateDesc(LocalDate from, LocalDate to, TransactionType type);
}
