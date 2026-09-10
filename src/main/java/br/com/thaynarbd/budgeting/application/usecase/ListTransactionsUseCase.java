package br.com.thaynarbd.budgeting.application.usecase;

import br.com.thaynarbd.budgeting.domain.model.Transaction;
import br.com.thaynarbd.budgeting.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ListTransactionsUseCase {
    private final TransactionRepository repository;
    public ListTransactionsUseCase(TransactionRepository repository) { this.repository = repository; }
    public List<Transaction> execute(LocalDate from, LocalDate to) {
        LocalDate resolvedFrom = from == null ? LocalDate.now().withDayOfMonth(1) : from;
        LocalDate resolvedTo = to == null ? LocalDate.now() : to;
        if (resolvedFrom.isAfter(resolvedTo)) throw new IllegalArgumentException("from não pode ser posterior a to");
        return repository.findAll(resolvedFrom, resolvedTo);
    }
}
