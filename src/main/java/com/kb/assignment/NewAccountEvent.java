package com.kb.assignment;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * 계좌 신설 이벤트
 * - 발생시각
 * - 고객번호
 * - 계좌번호
 */
public class NewAccountEvent extends Event {

    public NewAccountEvent(DateTime timestamp, String clientNo, String accountNo) throws ParseException {
        super(EventType.NEW_ACCOUNT, timestamp, clientNo, accountNo);
    }

    @Override
    public BigDecimal getAmount() {
        return new BigDecimal("0");
    }
}
