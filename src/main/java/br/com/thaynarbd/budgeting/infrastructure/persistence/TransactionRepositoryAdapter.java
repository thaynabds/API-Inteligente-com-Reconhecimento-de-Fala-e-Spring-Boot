package br.com.thaynarbd.budgeting.infrastructure.persistence;

import br.com.thaynarbd.budgeting.domain.model.Transaction;
import br.com.thaynarbd.budgeting.domain.repository.TransactionRepository;
import br.com.thaynarbd.budgeting.infrastructure.persistence.entity.TransactionEntity;
import br.com.thaynarbd.budgeting.infrastructure.persistence.repository.SpringDataTransactionRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TransactionRepositoryAdapter implements TransactionRepository {
    private final SpringDataTransactionRepository repository;
    public TransactionRepositoryAdapter(SpringDataTransactionRepository repository) { this.repository = repository; }

    @Override
    public Transaction save(Transaction transaction) {
        return repository.save(new TransactionEntity(transaction)).toDomain();
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return repository.findById(id).map(TransactionEntity::toDomain);
    }

    @Override
    public List<Transaction> findAll(LocalDate from, LocalDate to) {
        return repository.findByDateBetweenOrderByDateDesc(from, to).stream().map(TransactionEntity::toDomain).toList();
    }
}
