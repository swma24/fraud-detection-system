package com.kb.assignment;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class AppDemo implements Observer {

    private AccountManager accountManager;

    public AppDemo() {
        ArrayList<Rule>ruleList = new ArrayList<>();
        ruleList.add(new ARule());
        FraudDetector fraudDetector = new FraudDetector(ruleList);
        accountManager = new AccountManager(fraudDetector);
    }

    @Override
    public void update(Observable obs, Object obj) {
        if (obs instanceof EventGenerator && obj instanceof Event) {
            try {
                accountManager.processEvent((Event)obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {

            AppDemo appDemo = new AppDemo();

            EventGenerator eg1 = new EventGenerator();
            EventGenerator eg2 = new EventGenerator();
            EventGenerator eg3 = new EventGenerator();
            eg1.addObserver(appDemo);
            eg2.addObserver(appDemo);
            eg3.addObserver(appDemo);
            eg1.start();
            eg2.start();
            eg3.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
