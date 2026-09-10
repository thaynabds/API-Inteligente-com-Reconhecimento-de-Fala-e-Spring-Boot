package br.com.thaynarbd.budgeting.domain;

import br.com.thaynarbd.budgeting.domain.model.Transaction;
import br.com.thaynarbd.budgeting.domain.model.TransactionType;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    @Test
    void deveCriarTransacaoValida() {
        Transaction transaction = Transaction.create("Mercado", new BigDecimal("120.50"), TransactionType.EXPENSE, "alimentação", LocalDate.of(2026, 9, 10));
        assertNotNull(transaction.getId());
        assertEquals("Mercado", transaction.getDescription());
        assertEquals(new BigDecimal("120.50"), transaction.getAmount());
    }

    @Test
    void naoDeveAceitarValorNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                Transaction.create("Teste", new BigDecimal("-1"), TransactionType.EXPENSE, "outros", LocalDate.now()));
    }
}
