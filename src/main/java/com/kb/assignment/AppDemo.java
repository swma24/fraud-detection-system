package com.kb.assignment;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * 3개의 이벤트 생성기를 랜덤 이벤트 및 이상거래 이벤트를 생성하여 AppDemo 로 전달하고
 * AppDemo 는 해당 이벤트를 계좌별로 관리하고 이벤트마다 이상거래감지 서비스를 수행한다.
 *
 * - 출력내용
 * [Event for Fraud] ... -> 이상거래 감지 테스트를 위한 이벤트 정보 출력
 * [Event Count] ...     -> 발생 이벤트 100건마다 이벤트 타입별 건수 출력
 * [Alert] ...           -> 이상거래가 검지될 경우 관련 정보 출력
 */
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

            // 3개의 이벤트 생성기를 쓰레드로 생성하여 이벤트 생성.
            for (int i=0; i<3; ++i) {
                EventGenerator eg = new EventGenerator();
                eg.addObserver(appDemo); // 생성한 이벤트를 AppDemo 로 전달하기 위해 Observer 등록
                eg.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
