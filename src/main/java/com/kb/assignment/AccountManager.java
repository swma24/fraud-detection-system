package com.kb.assignment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * 계좌 관리자
 * - 잔액 및 이벤트 내역을 계좌번호별로 관리하고 후처리(이상거래 감지)를 수행한다.
 */
public class AccountManager {

    private HashMap<String, BigDecimal> accountBalances;    // 계좌 잔액
    private HashMap<String, ArrayList<Event>> eventHistory; // 전체 이벤트 내역
    private FraudDetector fraudDetector;                    // 이상거래 감지 서비스


    public AccountManager(FraudDetector fraudDetector) {
        this.accountBalances = new HashMap<>();
        this.eventHistory = new HashMap<>();
        this.fraudDetector = fraudDetector;
    }

    /**
     * 이벤트를 추가한다.
     * @param event
     * @throws Exception
     */
    public void addEvent(Event event) throws Exception {
        String accountNo = event.getAccountNo();
        ArrayList<Event> events = eventHistory.get(accountNo);
        if (events == null) { // 이벤트내역이 없을 경우 초기화
            eventHistory.put(accountNo, new ArrayList<>());
            events = eventHistory.get(accountNo);
        }

        events.add(event); // 이벤트 추가
        System.out.printf("timestamp: %s, type: %s, accountNo: %s, amount: %s\n", event.getTimestamp(), event.getType(), event.getAccountNo(), event.getAmount());

        // 이벤트에 따라 잔액 조정
        switch (event.getType()) {
            case NEW_ACCOUNT: // 계좌 개설
                if (events.size() > 1)
                    throw new Exception("already new account");
                accountBalances.put(accountNo, new BigDecimal("0"));
                break;
            case DEPOSIT: // 입금
                accountBalances.computeIfPresent(accountNo, (k, v) -> v.add(event.getAmount()));
                break;
            case WITHDRAWAL: // 출금
                accountBalances.computeIfPresent(accountNo, (k, v) -> v.subtract(event.getAmount()));
                break;
            case TRANSFER: // 이체
                accountBalances.computeIfPresent(accountNo, (k, v) -> v.subtract(event.getAmount()));
        }

        BigDecimal balance = accountBalances.get(accountNo);
        postProcess(balance, event, events); // 후처리(이상거래 감지)
    }

    /**
     * 후처리
     * @param balance 계좌잔액
     * @param event (새로 들어온) 현재 이벤트
     * @param events 해당 계좌의 이벤트 내역
     */
    private void postProcess(BigDecimal balance, Event event, ArrayList<Event> events) {
        fraudDetector.detectFraud(balance, event, events);
    }
}
