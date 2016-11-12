package com.kb.assignment;


import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 이상거래 감지 서비스
 * - 모든 규칙에 대하여 해당되는지를 판단.
 * - 모든 계좌정보 및 이벤트 내역을 DB가 아닌 Java Collections 로 관리한다 생각하고 코딩함.
 */

public class FraudDetector {

    private ArrayList<Rule> rules; // 규칙 리스트

    public FraudDetector(ArrayList<Rule> ruleList) {
        this.rules = ruleList;
    }

    /**
     * 이상거래를 감지한다.
     * @param balance 계좌 잔액
     * @param curEvent 현재 이벤트
     * @param events 해당 계좌의 이벤트 내역
     */
    public void detectFraud(BigDecimal balance, Event curEvent, ArrayList<Event> events) {
        // 해당 계좌의 데이터(balance, events)에 대하여 현재 이벤트의 이상거래 감지 처리가 완료된 후 다음 이벤트에 대하여 처리할 수있도록 동기화
        synchronized (balance) {
            synchronized (events) {
                for (Rule rule : rules) {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            boolean isFraud = rule.isFraud(balance, curEvent, events);
                            if (isFraud)
                                System.out.println("Alert! account " + curEvent.getAccountNo() + " is corresponds to " + rule.getName());
                        }
                    };
                    thread.start();
                }
            }
        }
    }

}
