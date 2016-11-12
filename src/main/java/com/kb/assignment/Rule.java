package com.kb.assignment;


import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * 규칙 추상화 클래스
 */
public abstract class Rule {

    private final String name;

    public Rule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isFraud(BigDecimal balance, Event curEvent, ArrayList<Event> eventHistory);
}
