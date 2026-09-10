package br.com.thaynarbd.budgeting.application.dto;

import br.com.thaynarbd.budgeting.domain.model.Transaction;
import br.com.thaynarbd.budgeting.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponse(UUID id, String description, BigDecimal amount,
                                  TransactionType type, String category, LocalDate date) {
    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(t.getId(), t.getDescription(), t.getAmount(), t.getType(), t.getCategory(), t.getDate());
    }
}
