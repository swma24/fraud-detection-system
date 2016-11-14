package com.kb.assignment;


import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Account {

    private BigDecimal balance;       // 계좌 잔액
    private ArrayList<Event> events;  // 전체 이벤트 내역

    public Account() {
        this.balance = new BigDecimal("0");
        this.events = new ArrayList<>();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) throws Exception {

        events.add(event); // 이벤트 추가
        System.out.printf("Event is added - timestamp: %s, accountNo: %s, type: %s, amount: %s\n", event.getTimestamp().toString("yyyy-MM-dd HH:mm:ss"), event.getAccountNo(), event.getType(), event.getAmount());

        // 이벤트에 따라 잔액 조정
        switch (event.getType()) {
//            case NEW_ACCOUNT: // 계좌 개설
//                if (events.size() > 1)
//                    throw new Exception("already new account");
//                break;
            case DEPOSIT: // 입금
                balance = balance.add(event.getAmount());
                break;
            case WITHDRAWAL: // 출금
                balance = balance.subtract(event.getAmount());
                break;
            case TRANSFER: // 이체
                balance = balance.subtract(event.getAmount());
        }

    }
}
