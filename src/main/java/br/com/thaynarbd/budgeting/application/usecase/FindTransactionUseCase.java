package br.com.thaynarbd.budgeting.application.usecase;

import br.com.thaynarbd.budgeting.domain.model.Transaction;
import br.com.thaynarbd.budgeting.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class FindTransactionUseCase {
    private final TransactionRepository repository;
    public FindTransactionUseCase(TransactionRepository repository) { this.repository = repository; }
    public Transaction execute(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada: " + id));
    }
}
