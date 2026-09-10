package br.com.thaynarbd.budgeting.infrastructure.persistence.entity;

import br.com.thaynarbd.budgeting.domain.model.Transaction;
import br.com.thaynarbd.budgeting.domain.model.TransactionType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_transactions_date", columnList = "transaction_date")
})
public class TransactionEntity {
    @Id
    private UUID id;
    @Column(nullable = false, length = 160)
    private String description;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;
    @Column(nullable = false, length = 80)
    private String category;
    @Column(name = "transaction_date", nullable = false)
    private LocalDate date;

    protected TransactionEntity() {}

    public TransactionEntity(Transaction transaction) {
        this.id = transaction.getId();
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.category = transaction.getCategory();
        this.date = transaction.getDate();
    }

    public Transaction toDomain() {
        return new Transaction(id, description, amount, type, category, date);
    }

    public UUID getId() { return id; }
}
