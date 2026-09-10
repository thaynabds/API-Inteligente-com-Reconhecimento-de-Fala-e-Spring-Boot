package br.com.thaynarbd.budgeting.infrastructure.web;

import br.com.thaynarbd.budgeting.application.dto.BalanceResponse;
import br.com.thaynarbd.budgeting.application.dto.CreateTransactionRequest;
import br.com.thaynarbd.budgeting.application.dto.TransactionResponse;
import br.com.thaynarbd.budgeting.application.usecase.CalculateBalanceUseCase;
import br.com.thaynarbd.budgeting.application.usecase.CreateTransactionUseCase;
import br.com.thaynarbd.budgeting.application.usecase.ListTransactionsUseCase;
import br.com.thaynarbd.budgeting.application.usecase.FindTransactionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final CreateTransactionUseCase create;
    private final ListTransactionsUseCase list;
    private final CalculateBalanceUseCase balance;
    private final FindTransactionUseCase find;

    public TransactionController(CreateTransactionUseCase create, ListTransactionsUseCase list, CalculateBalanceUseCase balance, FindTransactionUseCase find) {
        this.create = create; this.list = list; this.balance = balance; this.find = find;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(@Valid @RequestBody CreateTransactionRequest request) {
        return TransactionResponse.from(create.execute(request));
    }

    @GetMapping
    public List<TransactionResponse> list(@RequestParam(required = false) LocalDate from,
                                          @RequestParam(required = false) LocalDate to) {
        return list.execute(from, to).stream().map(TransactionResponse::from).toList();
    }

    @GetMapping("/{id}")
    public TransactionResponse findById(@PathVariable UUID id) {
        return TransactionResponse.from(find.execute(id));
    }

    @GetMapping("/balance")
    public BalanceResponse balance(@RequestParam(required = false) LocalDate from,
                                   @RequestParam(required = false) LocalDate to) {
        return balance.execute(from, to);
    }
}
