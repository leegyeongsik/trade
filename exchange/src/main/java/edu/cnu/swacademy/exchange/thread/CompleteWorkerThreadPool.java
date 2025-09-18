package edu.cnu.swacademy.exchange.thread;

import edu.cnu.swacademy.exchange.config.CompleteWorkerThreadSetting;
import edu.cnu.swacademy.exchange.process.OrderComplete;
import edu.cnu.swacademy.exchange.service.ExchangeService;
import org.springframework.stereotype.Component;

import java.util.Objects;
@Component
public class CompleteWorkerThreadPool {
    private final Thread[] workerThreads;
    public CompleteWorkerThreadPool(CompleteWorkerThreadSetting setting, OrderComplete orderComplete, ExchangeService exchangeService) {
        if(setting.getTotalThread() <1){
            throw new IllegalArgumentException("poolSize: > 0");
        }
        if(Objects.isNull(orderComplete)){
            throw new IllegalArgumentException("orderComplete null");
        }

        workerThreads = new Thread[setting.getTotalThread()];
        for(int i = 0; i< setting.getTotalThread(); i++){
            workerThreads[i] = new Thread(new OrderCompleteThread(orderComplete, exchangeService));
            workerThreads[i].setName(String.format("CompleteWorkerThread-%d",i));
        }
    }
    public synchronized void start(){
        for(Thread thread :workerThreads ){
            thread.start();
        }
    }

    public synchronized void stop(){
        for(Thread thread : workerThreads){
            if(Objects.nonNull(thread) && thread.isAlive()){
                thread.interrupt();
            }
        }
        for(Thread thread : workerThreads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
