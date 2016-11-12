package com.kb.assignment;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * 입금 이벤트
 * - 발생시각
 * - 고객번호
 * - 계좌번호
 * - 입금 금액
 */
public class DepositEvent extends Event {
    private BigDecimal amount; // 입금 금액

    public DepositEvent(String datetime, String clientNo, String accountNo, String amount) throws ParseException {
        super(EventType.DEPOSIT, datetime, clientNo, accountNo);
        this.amount = new BigDecimal(amount);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}
