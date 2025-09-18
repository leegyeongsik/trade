package edu.cnu.swacademy.exchange.common;

import edu.cnu.swacademy.exchange.thread.CompleteWorkerThreadPool;
import edu.cnu.swacademy.exchange.thread.RequestWorkerThread;
import edu.cnu.swacademy.exchange.thread.RequestWorkerThreadPool;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements ApplicationRunner {
    private final ApplicationContext applicationContext;
    public Initializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    @Override
    public void run(ApplicationArguments args) {
        applicationContext.getBean(RequestWorkerThreadPool.class).start();
        applicationContext.getBean(CompleteWorkerThreadPool.class).start();
    }
}