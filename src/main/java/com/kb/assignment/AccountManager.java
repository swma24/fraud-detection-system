package com.kb.assignment;

import java.util.HashMap;


/**
 * 계좌 관리자
 * - 잔액 및 이벤트 내역을 계좌번호별로 관리하고 후처리(이상거래 감지)를 수행한다.
 */
public class AccountManager {

    private HashMap<String, Account> accounts; //계좌 정보 (잔액, 이벤트 내역)
    private FraudDetector fraudDetector;       // 이상거래 감지 서비스


    public AccountManager(FraudDetector fraudDetector) {
        this.accounts = new HashMap<>();
        this.fraudDetector = fraudDetector;
    }

    /**
     * 이벤트를 처리한다.
     * @param event 입력된 이벤트
     * @throws Exception
     */
    public synchronized void processEvent(Event event) throws Exception {
        String accountNo = event.getAccountNo();
        Account acc = accounts.get(accountNo);

        if (acc == null) { // 계정정보가 없을 경우 초기화
            accounts.put(accountNo, new Account());
            acc = accounts.get(accountNo);
        }

        acc.addEvent(event); // 이벤트 추가

        postProcess(event, acc); // 후처리(이상거래 감지)
    }

    private void postProcess(Event event, Account acc) {
        fraudDetector.detectFraud(event, acc.getBalance(), acc.getEvents());
    }
}
