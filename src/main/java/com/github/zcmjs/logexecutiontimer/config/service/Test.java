package com.github.zcmjs.logexecutiontimer.config.service;

import com.github.zcmjs.logexecutiontimer.config.LogExecutionTime;
import org.springframework.stereotype.Component;

@Component
public class Test {
    @LogExecutionTime
    public void runJob(long duration) {
        try {
            System.out.println("Start Job by thread: " + Thread.currentThread().getName());
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @LogExecutionTime
    public void runJob2(long duration) {
        try {
            System.out.println("Start Job2 by thread: " + Thread.currentThread().getName());
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
