package com.kb.assignment;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * 출금 이벤트
 * - 발생시각
 * - 고객번호
 * - 계좌번호
 * - 출금 금액
 */
public class WithdrawalEvent extends Event {
    private BigDecimal amount; // 출금 금액

    public WithdrawalEvent(DateTime timestamp, String clientNo, String accountNo, long amount) throws ParseException {
        super(EventType.WITHDRAWAL, timestamp, clientNo, accountNo);
        this.amount = BigDecimal.valueOf(amount);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}
