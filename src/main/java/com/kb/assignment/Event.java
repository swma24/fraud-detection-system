package com.kb.assignment;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.text.ParseException;

enum EventType {
    NEW_ACCOUNT, TRANSFER, DEPOSIT, WITHDRAWAL
}

/**
 * 이벤트 추상화 클래스
 * - 발생시각
 * - 고객번호
 * - 계좌번호
 */
public abstract class Event {
    private final EventType type;   // 이벤트 타입
    private final DateTime timestamp;   // 발생시각
    private final String clientNo;  // 고객번호 (숫자 5자리 문자열)
    private final String accountNo; // 계좌번호 (숫자 7자리 문자열)

    public Event(EventType type, DateTime timestamp, String clientNo, String accountNo) throws ParseException {
        this.type = type;
        this.timestamp = timestamp;
        this.clientNo = clientNo;
        this.accountNo = accountNo;
    }

    public EventType getType() {
        return type;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public abstract BigDecimal getAmount();
}
