package br.com.thaynarbd.budgeting.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final String description;
    private final BigDecimal amount;
    private final TransactionType type;
    private final String category;
    private final LocalDate date;

    public Transaction(UUID id, String description, BigDecimal amount, TransactionType type,
                       String category, LocalDate date) {
        if (id == null) throw new IllegalArgumentException("id é obrigatório");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("description é obrigatório");
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("amount deve ser maior que zero");
        if (type == null) throw new IllegalArgumentException("type é obrigatório");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("category é obrigatória");
        if (date == null) throw new IllegalArgumentException("date é obrigatória");
        this.id = id;
        this.description = description.trim();
        this.amount = amount;
        this.type = type;
        this.category = category.trim();
        this.date = date;
    }

    public static Transaction create(String description, BigDecimal amount, TransactionType type,
                                     String category, LocalDate date) {
        return new Transaction(UUID.randomUUID(), description, amount, type, category, date);
    }

    public UUID getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
}
