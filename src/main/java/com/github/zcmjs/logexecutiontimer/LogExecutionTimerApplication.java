package com.github.zcmjs.logexecutiontimer;

import com.github.zcmjs.logexecutiontimer.config.service.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class LogExecutionTimerApplication implements ApplicationRunner {
    @Autowired
    private Test test;

    public static void main(String[] args) {
        SpringApplication.run(LogExecutionTimerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        executor.scheduleWithFixedDelay(() -> {
            long duration = ThreadLocalRandom.current().nextLong(2_000, 5_000);
            test.runJob(duration);
        }, 0, 1, TimeUnit.SECONDS);

        executor.scheduleWithFixedDelay(() -> {
            long duration = ThreadLocalRandom.current().nextLong(7_000, 12_000);
            test.runJob2(duration);
        }, 0, 2, TimeUnit.SECONDS);
    }
}
