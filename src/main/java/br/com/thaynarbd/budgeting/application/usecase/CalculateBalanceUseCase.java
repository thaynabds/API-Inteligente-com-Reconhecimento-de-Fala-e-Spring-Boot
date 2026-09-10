package br.com.thaynarbd.budgeting.application.usecase;

import br.com.thaynarbd.budgeting.application.dto.BalanceResponse;
import br.com.thaynarbd.budgeting.domain.model.Transaction;
import br.com.thaynarbd.budgeting.domain.model.TransactionType;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CalculateBalanceUseCase {
    private final ListTransactionsUseCase listTransactionsUseCase;
    public CalculateBalanceUseCase(ListTransactionsUseCase listTransactionsUseCase) { this.listTransactionsUseCase = listTransactionsUseCase; }
    public BalanceResponse execute(LocalDate from, LocalDate to) {
        LocalDate resolvedFrom = from == null ? LocalDate.now().withDayOfMonth(1) : from;
        LocalDate resolvedTo = to == null ? LocalDate.now() : to;
        List<Transaction> transactions = listTransactionsUseCase.execute(resolvedFrom, resolvedTo);
        BigDecimal income = transactions.stream().filter(t -> t.getType() == TransactionType.INCOME).map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expense = transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE).map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new BalanceResponse(resolvedFrom, resolvedTo, income, expense, income.subtract(expense), transactions.size());
    }
}
