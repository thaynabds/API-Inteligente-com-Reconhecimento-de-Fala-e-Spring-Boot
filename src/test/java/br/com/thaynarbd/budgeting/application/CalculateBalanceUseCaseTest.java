package br.com.thaynarbd.budgeting.application;

import br.com.thaynarbd.budgeting.application.usecase.CalculateBalanceUseCase;
import br.com.thaynarbd.budgeting.application.usecase.ListTransactionsUseCase;
import br.com.thaynarbd.budgeting.domain.model.Transaction;
import br.com.thaynarbd.budgeting.domain.model.TransactionType;
import br.com.thaynarbd.budgeting.domain.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateBalanceUseCaseTest {
    @Test
    void deveCalcularSaldo() {
        TransactionRepository repository = new FakeRepository(List.of(
                Transaction.create("Salário", new BigDecimal("5000.00"), TransactionType.INCOME, "salário", LocalDate.of(2026, 9, 1)),
                Transaction.create("Aluguel", new BigDecimal("1800.00"), TransactionType.EXPENSE, "moradia", LocalDate.of(2026, 9, 5)),
                Transaction.create("Mercado", new BigDecimal("350.00"), TransactionType.EXPENSE, "alimentação", LocalDate.of(2026, 9, 6))
        ));
        var list = new ListTransactionsUseCase(repository);
        var balance = new CalculateBalanceUseCase(list).execute(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30));

        assertEquals(new BigDecimal("5000.00"), balance.totalIncome());
        assertEquals(new BigDecimal("2150.00"), balance.totalExpense());
        assertEquals(new BigDecimal("2850.00"), balance.balance());
        assertEquals(3, balance.transactionCount());
    }

    private record FakeRepository(List<Transaction> transactions) implements TransactionRepository {
        public Transaction save(Transaction transaction) { return transaction; }
        public Optional<Transaction> findById(UUID id) { return transactions.stream().filter(t -> t.getId().equals(id)).findFirst(); }
        public List<Transaction> findAll(LocalDate from, LocalDate to) {
            return transactions.stream().filter(t -> !t.getDate().isBefore(from) && !t.getDate().isAfter(to)).toList();
        }
    }
}
