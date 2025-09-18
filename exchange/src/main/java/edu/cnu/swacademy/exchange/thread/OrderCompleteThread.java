package edu.cnu.swacademy.exchange.thread;

import edu.cnu.swacademy.exchange.process.OrderComplete;
import edu.cnu.swacademy.exchange.service.ExchangeService;
import org.springframework.stereotype.Component;

@Component
public class OrderCompleteThread implements Runnable{
    private final OrderComplete orderComplete;
    private final ExchangeService exchangeService;
    public OrderCompleteThread(OrderComplete orderComplete, ExchangeService exchangeService) {
        this.orderComplete = orderComplete;
        this.exchangeService = exchangeService;
    }

    @Override
    public void run() {
         exchangeService.orderComplete(orderComplete.poll());
    }
}
