package edu.cnu.swacademy.exchange.process;

import edu.cnu.swacademy.exchange.dto.ExchangeResponse;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class OrderComplete {
    BlockingQueue<ExchangeResponse> completeQueue = new LinkedBlockingQueue<>();

    void offer(ExchangeResponse exchangeResponse){
        completeQueue.offer(exchangeResponse)
    }
    public ExchangeResponse poll(){
        try {
            return completeQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
