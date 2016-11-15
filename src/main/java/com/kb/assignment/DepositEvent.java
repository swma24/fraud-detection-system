package com.kb.assignment;

import org.joda.time.DateTime;

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

    public DepositEvent(DateTime timestamp, String clientNo, String accountNo, long amount) throws ParseException {
        super(EventType.DEPOSIT, timestamp, clientNo, accountNo);
        this.amount = BigDecimal.valueOf(amount);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}
