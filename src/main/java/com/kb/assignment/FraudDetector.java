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
     * @param curEvent 현재 이벤트
     * @param balance 계좌 잔액
     * @param events 해당 계좌의 이벤트 내역
     */
    public void detectFraud(Event curEvent, BigDecimal balance, ArrayList<Event> events) {
        for (Rule rule : rules) {
            Thread thread = new Thread(() -> {
                boolean isFraud = rule.isFraud(balance, curEvent, events);
                if (isFraud)
                    System.out.println("Alert! account " + curEvent.getAccountNo() + " is corresponds to " + rule.getName());
            });
            thread.start();
        }
    }

}
