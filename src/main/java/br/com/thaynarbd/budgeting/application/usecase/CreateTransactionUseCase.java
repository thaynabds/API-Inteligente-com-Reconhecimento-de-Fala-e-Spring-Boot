package br.com.thaynarbd.budgeting.application.usecase;

import br.com.thaynarbd.budgeting.application.dto.CreateTransactionRequest;
import br.com.thaynarbd.budgeting.domain.model.Transaction;
import br.com.thaynarbd.budgeting.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class CreateTransactionUseCase {
    private final TransactionRepository repository;
    public CreateTransactionUseCase(TransactionRepository repository) { this.repository = repository; }
    public Transaction execute(CreateTransactionRequest request) {
        LocalDate date = request.date() == null ? LocalDate.now() : request.date();
        return repository.save(Transaction.create(request.description(), request.amount(), request.type(), request.category(), date));
    }
}
