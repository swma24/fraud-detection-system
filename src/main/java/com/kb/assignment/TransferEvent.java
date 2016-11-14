package com.kb.assignment;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * 이체 이벤트
 * - 발생시각
 * - 고객번호
 * - 송금 계좌번호
 * - 송금 이체전 계좌잔액
 * - 수취 은행
 * - 수취 계좌주
 * - 이체 금액
 */
public class TransferEvent extends Event {

    private BigDecimal prevBalance;         // 이체전 계좌잔액
    private String receivingBank;           // 수취 은행
    private String receivingAccountOwner;   // 수취 계좌주
    private BigDecimal amount;              // 이체 금액

    public TransferEvent(DateTime timestamp, String clientNo, String accountNo, long prevBalance,
                         String receivingBank, String receivingAccountOwner, long amount) throws ParseException {
        super(EventType.TRANSFER, timestamp, clientNo, accountNo);
        this.prevBalance = BigDecimal.valueOf(prevBalance);
        this.receivingBank = receivingBank;
        this.receivingAccountOwner = receivingAccountOwner;
        this.amount = BigDecimal.valueOf(amount);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}
