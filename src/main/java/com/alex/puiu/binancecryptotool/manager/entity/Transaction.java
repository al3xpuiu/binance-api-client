package com.alex.puiu.binancecryptotool.manager.entity;

import com.alex.puiu.binancecryptotool.util.Action;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Transaction {

    private long time;
    private BigDecimal value;
    private BigDecimal quantity;
    private Action action;
    private Currency currency;

    public Transaction() {
        this.value = BigDecimal.ZERO;
        this.quantity = BigDecimal.ZERO;
    }
}
