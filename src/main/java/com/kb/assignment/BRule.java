package com.kb.assignment;


import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * 규칙 B
 */
public class BRule extends Rule {

    public BRule() {
        super("Rule B");
    }

    @Override
    public boolean isFraud(BigDecimal balance, Event curEvent, ArrayList<Event> events) {
        //...

        return false;
    }
}
