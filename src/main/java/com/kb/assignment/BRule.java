package com.kb.assignment;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * 규칙 B
 */
public class BRule extends Rule {

    public BRule() {
        super("Rule B");
    }

    @Override
    public boolean isFraud(BigDecimal balance, Event curEvent, ArrayList<Event> events) {
//
//        EventType type = curEvent.getType();
//        if ((type == EventType.WITHDRAWAL || type == EventType.TRANSFER)
//                && balance.compareTo(new BigDecimal("10000")) <= 0) { // 출금 또는 이체, 잔액 1만원 이하
//
//            Event e = events.get(0); // 계좌개설 이벤트
//            Event e2 = events.get(1); // 계좌개설 후 이벤트
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.DATE, 7);
//            Date sevenDaysAgo = calendar.getTime();
//            if (e.getType() == EventType.NEW_ACCOUNT && e.getTimestamp().compareTo(sevenDaysAgo) <= 0
//                    && e2.getType() == EventType.DEPOSIT) { // 7일 이내 개설된 계좌 및 입금
//
//                BigDecimal depositAmount = e2.getAmount();
//                if (depositAmount.compareTo(new BigDecimal("900000")) >= 0
//                        && depositAmount.compareTo(new BigDecimal("1000000")) <= 0) // 90~100 만원 입금
//                    return true;
//            }
//        }

        return false;
    }
}
