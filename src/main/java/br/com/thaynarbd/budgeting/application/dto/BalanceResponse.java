package br.com.thaynarbd.budgeting.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceResponse(LocalDate from, LocalDate to, BigDecimal totalIncome,
                              BigDecimal totalExpense, BigDecimal balance, long transactionCount) {}
