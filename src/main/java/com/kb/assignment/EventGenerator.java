package com.kb.assignment;


import org.joda.time.DateTime;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventGenerator extends Observable implements Runnable {

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

    private static DateTime eventTimestamp;
    private static ConcurrentHashMap<String, Tuple> accountBalances;    // 계좌 잔액 (이벤트 정합성을 맞추기 위함)
    private static int threadCount;

    private int sleepMillis;
    private Thread thread;
    private String threadName;
    private Random rand;
    private static long eventCount;

    private static int fraudEventTypeCount;
    private static String fraudClientNo;
    private static String fraudAccountNo;

    public EventGenerator() throws ParseException {
        eventTimestamp = new DateTime().minusDays(7);
        accountBalances = new ConcurrentHashMap<>();
        ++threadCount;
        this.sleepMillis = 100;
        this.threadName = "Thread " + threadCount;
        this.rand = new Random();
        this.thread = new Thread(this);

        fraudEventTypeCount = 0;
        eventCount = 0;
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
        return String.valueOf((int) (Math.random() * 9000000) + 1000000);
    }


    // 이상거래 이벤트 리스트 생성
    private Event getFraudEvent() {
        Event event = null;

        try {
            switch (fraudEventTypeCount) {
                case 0:
                    fraudClientNo = getRandomClientNo();
                    fraudAccountNo = getRandomAccountNo();
                    event = new NewAccountEvent(eventTimestamp, fraudClientNo, fraudAccountNo);
                    accountBalances.put(fraudAccountNo, new Tuple(fraudClientNo, new BigDecimal("0")));
                    break;
                case 1:
                    event = new DepositEvent(eventTimestamp, fraudClientNo, fraudAccountNo, 950000);
                    accountBalances.computeIfPresent(fraudAccountNo, (k, v) -> v.getAdjustedTuple(950000));
                    break;
                case 2:
                    event = new WithdrawalEvent(eventTimestamp, fraudClientNo, fraudAccountNo, 950000);
                    accountBalances.computeIfPresent(fraudAccountNo, (k, v) -> v.getAdjustedTuple(950000));
                    fraudEventTypeCount = 0;
                    break;
            }

            eventTimestamp = eventTimestamp.plusSeconds(100);
            ++fraudEventTypeCount;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }

    private void createEvent() {

        try {
            Event event = null;

            ++eventCount;

            if (eventCount % 500 == 0) {
                event = getFraudEvent();
            } else {
                String randomAccountNo = getRandomAccountNo();
                Tuple t = accountBalances.get(randomAccountNo);
                if (t == null) {
                    String randomClientNo = getRandomClientNo();
                    event = new NewAccountEvent(eventTimestamp, randomClientNo, randomAccountNo);
                    accountBalances.put(randomAccountNo, new Tuple(randomClientNo, new BigDecimal("0")));

                } else {
                    int whichEventType = rand.nextInt(3);
                    switch (whichEventType) {
                        case 0:
                            long depositAmount = rand.nextInt(1000) * 10000;
                            event = new DepositEvent(eventTimestamp, accountBalances.get(randomAccountNo).clientNo, randomAccountNo, depositAmount);
                            accountBalances.computeIfPresent(randomAccountNo, (k, v) -> v.getAdjustedTuple(depositAmount));
                            break;
                        case 1:
                            long withdrawalAmount = rand.nextInt(t.balance.intValue()) + 1;
                            event = new WithdrawalEvent(eventTimestamp, accountBalances.get(randomAccountNo).clientNo, randomAccountNo, withdrawalAmount);
                            accountBalances.computeIfPresent(randomAccountNo, (k, v) -> v.getAdjustedTuple(withdrawalAmount));
                            break;
                        case 2:
                            long transferAmount = rand.nextInt(t.balance.intValue()) + 1;
                            event = new TransferEvent(eventTimestamp, accountBalances.get(randomAccountNo).clientNo, randomAccountNo, t.balance.intValue(), "kakao bank", "kakao", transferAmount);
                            accountBalances.computeIfPresent(randomAccountNo, (k, v) -> v.getAdjustedTuple(transferAmount));
                            break;
                    }
                }
            }

            setChanged();
            notifyObservers(event);
            sleep();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        createEvent();
    }

    public void start() throws InterruptedException {
        sleep();
        thread.start();
    }
}
