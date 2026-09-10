package br.com.thaynarbd.budgeting.infrastructure.ai;

import br.com.thaynarbd.budgeting.application.dto.BalanceResponse;
import br.com.thaynarbd.budgeting.application.dto.CreateTransactionRequest;
import br.com.thaynarbd.budgeting.application.usecase.CalculateBalanceUseCase;
import br.com.thaynarbd.budgeting.application.usecase.CreateTransactionUseCase;
import br.com.thaynarbd.budgeting.application.usecase.ListTransactionsUseCase;
import br.com.thaynarbd.budgeting.domain.model.TransactionType;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class FinancialTools {
    private final CreateTransactionUseCase create;
    private final ListTransactionsUseCase list;
    private final CalculateBalanceUseCase balance;

    public FinancialTools(CreateTransactionUseCase create, ListTransactionsUseCase list, CalculateBalanceUseCase balance) {
        this.create = create;
        this.list = list;
        this.balance = balance;
    }

    @Tool(name = "create_transaction", description = "Cria uma transação financeira. Use para registrar receita ou despesa informada pela pessoa usuária. amount deve ser um número positivo em reais; type deve ser INCOME para receita ou EXPENSE para despesa; date deve estar em ISO-8601 (AAAA-MM-DD).")
    public Map<String, Object> createTransaction(
            @ToolParam(description = "Descrição curta da transação") String description,
            @ToolParam(description = "Valor positivo em reais") BigDecimal amount,
            @ToolParam(description = "INCOME para receita ou EXPENSE para despesa") String type,
            @ToolParam(description = "Categoria financeira, por exemplo alimentação, moradia, transporte ou salário") String category,
            @ToolParam(description = "Data da transação no formato AAAA-MM-DD") String date) {
        LocalDate transactionDate = date == null || date.isBlank() ? LocalDate.now() : LocalDate.parse(date);
        var transaction = create.execute(new CreateTransactionRequest(description, amount, TransactionType.valueOf(type.toUpperCase()), category, transactionDate));
        return Map.of(
                "id", transaction.getId().toString(),
                "description", transaction.getDescription(),
                "amount", transaction.getAmount(),
                "type", transaction.getType().name(),
                "category", transaction.getCategory(),
                "date", transaction.getDate().toString()
        );
    }

    @Tool(name = "list_transactions", description = "Consulta transações financeiras em um intervalo. Use quando a pessoa pedir histórico, gastos ou receitas. Retorne datas e valores para apoiar a resposta.")
    public List<Map<String, Object>> listTransactions(
            @ToolParam(description = "Data inicial AAAA-MM-DD") String from,
            @ToolParam(description = "Data final AAAA-MM-DD") String to) {
        LocalDate start = from == null || from.isBlank() ? LocalDate.now().withDayOfMonth(1) : LocalDate.parse(from);
        LocalDate end = to == null || to.isBlank() ? LocalDate.now() : LocalDate.parse(to);
        return list.execute(start, end).stream().map(t -> Map.<String, Object>of(
                "id", t.getId().toString(), "description", t.getDescription(), "amount", t.getAmount(),
                "type", t.getType().name(), "category", t.getCategory(), "date", t.getDate().toString()
        )).toList();
    }

    @Tool(name = "get_balance", description = "Calcula o total de receitas, despesas e saldo no período informado. Use para perguntas como 'qual meu saldo?', 'quanto gastei?' ou 'como estou neste mês?'.")
    public BalanceResponse getBalance(
            @ToolParam(description = "Data inicial AAAA-MM-DD") String from,
            @ToolParam(description = "Data final AAAA-MM-DD") String to) {
        LocalDate start = from == null || from.isBlank() ? LocalDate.now().withDayOfMonth(1) : LocalDate.parse(from);
        LocalDate end = to == null || to.isBlank() ? LocalDate.now() : LocalDate.parse(to);
        return balance.execute(start, end);
    }
}
