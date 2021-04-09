package priv.yjs.umbrellasharing.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


class OrderServiceTest {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(now.plusMinutes(20), now);
        System.out.println(String.valueOf(minutes));

        System.out.println(String.valueOf(68L / 30));
        System.out.println(String.valueOf(29L / 30));
    }

}