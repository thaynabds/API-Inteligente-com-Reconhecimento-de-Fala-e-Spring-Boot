package br.com.thaynarbd.budgeting.application.dto;

import br.com.thaynarbd.budgeting.domain.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(
        @NotBlank(message = "description é obrigatória") String description,
        @NotNull(message = "amount é obrigatório") @DecimalMin(value = "0.01", message = "amount deve ser maior que zero") BigDecimal amount,
        @NotNull(message = "type é obrigatório") TransactionType type,
        @NotBlank(message = "category é obrigatória") String category,
        LocalDate date
) {}
