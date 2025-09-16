package edu.cnu.swacademy.exchange.common;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements ApplicationRunner {
    public Initializer() {
    }
    @Override
    public void run(ApplicationArguments args) { // StockQueueSet 초기화
    }
}