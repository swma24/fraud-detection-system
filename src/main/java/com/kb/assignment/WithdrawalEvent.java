package com.kb.assignment;

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

    public WithdrawalEvent(String datetime, String clientNo, String accountNo, String amount) throws ParseException {
        super(EventType.WITHDRAWAL, datetime, clientNo, accountNo);
        this.amount = new BigDecimal(amount);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}
