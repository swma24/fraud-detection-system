package com.kb.assignment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private final Date timestamp;   // 발생시각
    private final String clientNo;  // 고객번호
    private final String accountNo; // 계좌번호

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public Event(EventType type, String datetime, String clientNo, String accountNo) throws ParseException {
        this.type = type;
        this.timestamp = dateFormat.parse(datetime);
        this.clientNo = clientNo;
        this.accountNo = accountNo;
    }

    public EventType getType() {
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public abstract BigDecimal getAmount();
}
