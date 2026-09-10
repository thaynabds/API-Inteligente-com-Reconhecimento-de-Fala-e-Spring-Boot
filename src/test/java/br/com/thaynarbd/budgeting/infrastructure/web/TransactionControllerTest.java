package br.com.thaynarbd.budgeting.infrastructure.web;

import br.com.thaynarbd.budgeting.application.dto.TransactionResponse;
import br.com.thaynarbd.budgeting.application.usecase.CalculateBalanceUseCase;
import br.com.thaynarbd.budgeting.application.usecase.CreateTransactionUseCase;
import br.com.thaynarbd.budgeting.application.usecase.FindTransactionUseCase;
import br.com.thaynarbd.budgeting.application.usecase.ListTransactionsUseCase;
import br.com.thaynarbd.budgeting.domain.model.Transaction;
import br.com.thaynarbd.budgeting.domain.model.TransactionType;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TransactionControllerTest {
    @Test
    void deveCriarTransacaoPeloController() {
        var create = mock(CreateTransactionUseCase.class);
        var list = mock(ListTransactionsUseCase.class);
        var balance = mock(CalculateBalanceUseCase.class);
        var find = mock(FindTransactionUseCase.class);
        var controller = new TransactionController(create, list, balance, find);
        var transaction = Transaction.create("Café", new BigDecimal("8.50"), TransactionType.EXPENSE, "alimentação", LocalDate.of(2026, 9, 10));
        when(create.execute(any())).thenReturn(transaction);

        TransactionResponse response = controller.create(new br.com.thaynarbd.budgeting.application.dto.CreateTransactionRequest("Café", new BigDecimal("8.50"), TransactionType.EXPENSE, "alimentação", LocalDate.of(2026, 9, 10)));

        assertNotNull(response.id());
        assertEquals("Café", response.description());
        verify(create).execute(any());
    }
}
