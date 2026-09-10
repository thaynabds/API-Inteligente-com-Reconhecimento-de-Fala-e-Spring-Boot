package br.com.thaynarbd.budgeting.domain.repository;

import br.com.thaynarbd.budgeting.domain.model.Transaction;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findAll(LocalDate from, LocalDate to);
}
