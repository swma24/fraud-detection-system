package com.kb.assignment;

import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        try {

            ArrayList<Rule> ruleList = new ArrayList<>();
            ruleList.add(new ARule());
            FraudDetector fraudDetector = new FraudDetector(ruleList);

            AccountManager accountManager = new AccountManager(fraudDetector);

            String testClientNo = "00001";
            String testAccountNo = "10012345";
            accountManager.addEvent(new NewAccountEvent("2016-11-5 10:00:00", testClientNo, testAccountNo)); // 계좌개설
            accountManager.addEvent(new DepositEvent("2016-11-7 10:00:00", testClientNo, testAccountNo, "1000000")); // 입금
            accountManager.addEvent(new WithdrawalEvent("2016-11-7 11:00:00", testClientNo, testAccountNo, "999500")); // 출금


            String testClientNo2 = "00001";
            String testAccountNo2 = "10012347";
            accountManager.addEvent(new NewAccountEvent("2016-11-5 10:00:00", testClientNo2, testAccountNo2)); // 계좌개설
            accountManager.addEvent(new DepositEvent("2016-11-7 10:00:00", testClientNo2, testAccountNo2, "1000000")); // 입금
            accountManager.addEvent(new WithdrawalEvent("2016-11-7 11:00:00", testClientNo2, testAccountNo2, "900000")); // 출금

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
