package com.example.herokudemo;


import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class HerokuDemoApplication {

    private static AFwebService afWebService = new AFwebService();
    private static RESTtimer restTimer = new RESTtimer();

    public static void main(String[] args) {
        SpringApplication.run(HerokuDemoApplication.class, args);
    }
    public static int timerSchCnt = 0;
    public static boolean init = false;

    // just for testing to use 1 minute delay
    @Scheduled(fixedDelay = 10000) //60000) //2000)
    public void scheduleTaskWithFixedDelay() {

        timerSchCnt++;
        if (timerSchCnt < 0) {
            timerSchCnt = 100;
        }

        try {
            restTimer.RestTimerHandler();
            TimeUnit.SECONDS.sleep(1);

        } catch (Exception ex) {

        }
    }

}
