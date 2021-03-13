package com.alex.puiu.binancecryptotool.entity;

import com.alex.puiu.binancecryptotool.util.Action;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Currency {

    private String symbol;

    private BigDecimal quantity;

    private BigDecimal value;

    private BigDecimal valueProUnit;

    private final Set<Transaction> transactions;

    public Currency() {
        this.transactions = new HashSet<>();
        this.quantity = BigDecimal.ZERO;
        this.value = BigDecimal.ZERO;
        this.valueProUnit = BigDecimal.ZERO;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            Action action = transaction.getAction();
            if (action == null) {
                throw new IllegalStateException("Every transaction must have an action");
            }
            transaction.setCurrency(this);
            this.transactions.add(transaction);
            switch (action) {
                case BUY:
                    this.quantity = this.quantity.add(transaction.getQuantity());
                    this.value = this.value.add(transaction.getValue());
                    break;
                case SELL:
                    this.quantity = this.quantity.subtract(transaction.getQuantity());
                    this.value = this.value.subtract(transaction.getValue());
            }
            this.valueProUnit = this.value.divide(this.quantity,8, RoundingMode.CEILING);
        }
    }
}
