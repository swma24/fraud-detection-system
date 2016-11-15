package com.kb.assignment;


import org.joda.time.DateTime;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 이벤트 생성기
 */
public class EventGenerator extends Observable implements Runnable {

    private static DateTime eventTimestamp = new DateTime().minusDays(7); // 오늘 기준으로 7일 전부터 이벤트 생성 시작시각으로 설정
    private static ConcurrentHashMap<String, Tuple> accounts = new ConcurrentHashMap<>();    // 계좌정보 관리 맵 (이벤트 정합성을 맞추기 위함)
    private static int threadCount = 0;

    private int sleepMillis;
    private Thread thread;
    private String threadName;
    private Random rand;

    private static long totalEventCount = 0;
    private static long newAccountEventCount = 0;
    private static long depositEventCount = 0;
    private static long withdrawalEventCount = 0;
    private static long transferEventCount = 0;

    private static int fraudEventTypeCount = 0;
    private static String fraudClientNo;
    private static String fraudAccountNo;

    /**
     * 계좌번호에 따른 계좌정보를 관리하기 위한 Tuple
     */
    private class Tuple {
        final String clientNo;
        final BigDecimal balance;
        Tuple(String clientNo, BigDecimal balance) {
            this.clientNo = clientNo;
            this.balance = balance;
        }

        private Tuple getAdjustedTuple(long adjustment) {
            return new Tuple(this.clientNo, this.balance.add(BigDecimal.valueOf(adjustment)));
        }
    }

    public EventGenerator() throws ParseException {
        ++threadCount;
        this.sleepMillis = 50;
        this.threadName = "Thread " + threadCount;
        this.rand = new Random();
        this.thread = new Thread(this);
    }

    /**
     *  sleepMillis 를 기준으로 +-5 sleepMillis 이내 임의의 값 만큼 sleep.
     */
    private void sleep() {
        try {
            Thread.sleep(rand.nextInt(sleepMillis) + sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getRandomClientNo() {
        return String.valueOf((int) (Math.random() * 90000) + 10000);
    }

    private String getRandomAccountNo() {
        return "1000" + String.valueOf((int) (Math.random() * 900) + 100);
    }


    /**
     * 이상거래 이벤트 생성
     */
    private Event getFraudEvent(String accountNo) {
        Event event = null;

        try {
            switch (fraudEventTypeCount) {
                case 0:
                    fraudClientNo = getRandomClientNo();
                    fraudAccountNo = accountNo;
                    event = new NewAccountEvent(eventTimestamp, fraudClientNo, fraudAccountNo);
                    accounts.put(fraudAccountNo, new Tuple(fraudClientNo, new BigDecimal("0")));
                    break;
                case 1:
                    event = new DepositEvent(eventTimestamp, fraudClientNo, fraudAccountNo, 950000);
                    accounts.computeIfPresent(fraudAccountNo, (k, v) -> v.getAdjustedTuple(950000));
                    break;
                case 2:
                    event = new WithdrawalEvent(eventTimestamp, fraudClientNo, fraudAccountNo, 950000);
                    accounts.computeIfPresent(fraudAccountNo, (k, v) -> v.getAdjustedTuple(950000));
                    fraudEventTypeCount = 0;
                    break;
            }

            ++fraudEventTypeCount;
            if (fraudEventTypeCount > 2)
                fraudEventTypeCount = 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.printf("[Event for Fraud] timestamp: %s, accountNo: %s, type: %s, amount: %s\n",
                event.getTimestamp().toString("yyyy-MM-dd HH:mm:ss"), event.getAccountNo(), event.getType(), event.getAmount());

        return event;
    }

    /**
     * 랜덤 이벤트 또는 이상거래 이벤트를 생성하여 이벤트를 처리하는 AppDemo 로 전달.
     */
    private void createEvent() {

        try {
            Event event = null;

            // 랜덤 계좌번호를 추출하고 계좌가 개설되었는지 확인한다.
            String randomAccountNo = getRandomAccountNo();
            Tuple t = accounts.get(randomAccountNo);

            synchronized (eventTimestamp) {
                // 전체 발생 이벤트 건수 70개 마다
                if (totalEventCount % 70 == 0) { // 이상거래 이벤트 생성
                    if (fraudEventTypeCount == 0) {
                        while (t != null) {
                            randomAccountNo = getRandomAccountNo();
                            t = accounts.get(randomAccountNo);
                        }
                    }
                    event = getFraudEvent(randomAccountNo);

                } else { // 아니면, 랜덤 이벤트 생성

                    int whichEventType;
                    if (t == null) {
                        whichEventType = 0;
                    } else {
                        if (t.balance.compareTo(BigDecimal.valueOf(0)) == 0) {
                            whichEventType = 1;
                        } else {
                            whichEventType = rand.nextInt(2) + 2;
                        }
                    }

                    switch (whichEventType) {
                        case 0: // 계좌 개설
                            String randomClientNo = getRandomClientNo();
                            event = new NewAccountEvent(eventTimestamp, randomClientNo, randomAccountNo);
                            accounts.put(randomAccountNo, new Tuple(randomClientNo, new BigDecimal("0")));
                            break;
                        case 1: // 입금
                            long depositAmount = rand.nextInt(1000) * 10000;
                            event = new DepositEvent(eventTimestamp, accounts.get(randomAccountNo).clientNo, randomAccountNo, depositAmount);
                            accounts.computeIfPresent(randomAccountNo, (k, v) -> v.getAdjustedTuple(depositAmount));
                            break;
                        case 2: // 출금
                            long withdrawalAmount = rand.nextInt(t.balance.intValue()) + 1;
                            event = new WithdrawalEvent(eventTimestamp, accounts.get(randomAccountNo).clientNo, randomAccountNo, withdrawalAmount);
                            accounts.computeIfPresent(randomAccountNo, (k, v) -> v.getAdjustedTuple(withdrawalAmount));
                            break;
                        case 3: // 이체
                            long transferAmount = rand.nextInt(t.balance.intValue()) + 1;
                            event = new TransferEvent(eventTimestamp, accounts.get(randomAccountNo).clientNo, randomAccountNo, t.balance.intValue(), "kakao bank", "kakao", transferAmount);
                            accounts.computeIfPresent(randomAccountNo, (k, v) -> v.getAdjustedTuple(transferAmount));
                            break;
                    }
                }

                eventTimestamp = eventTimestamp.plusSeconds(100); // 이벤트 발생시각 업데이트
            }

            switch (event.getType()) {
                case NEW_ACCOUNT:
                    ++newAccountEventCount;
                    break;
                case DEPOSIT:
                    ++depositEventCount;
                    break;
                case WITHDRAWAL:
                    ++withdrawalEventCount;
                    break;
                case TRANSFER:
                    ++transferEventCount;
                    break;
            }
            ++totalEventCount;

            if (totalEventCount % 100 == 0) {
                System.out.printf("[Event Count] - total: %d, new account: %d, deposit: %d, withdrawal: %d, transfer: %d\n",
                        totalEventCount, newAccountEventCount, depositEventCount, withdrawalEventCount, transferEventCount);
            }

            // AppDemo 에 이벤트 전달
            setChanged();
            notifyObservers(event);

            sleep();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            createEvent();
        }
    }

    public void start() throws InterruptedException {
        sleep();
        thread.start();
    }
}
