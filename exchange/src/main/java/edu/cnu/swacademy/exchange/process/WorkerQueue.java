package edu.cnu.swacademy.exchange.process;


import edu.cnu.swacademy.exchange.domain.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
@Component
public class WorkerQueue {
    private final StockQueueSet stockQueueSet;
    BlockingQueue<Integer> workerQueue = new LinkedBlockingQueue<>();
    public WorkerQueue(StockQueueSet stockQueueSet) {
        this.stockQueueSet = stockQueueSet;
    }

    public void orderOffer(Order order){
        stockQueueSet.offerStockOrder(order.getProduct_id(),order);
        boolean offer = workerQueue.offer(order.getProduct_id());
    }
    public void orderPoll(){
        try {
            int stock = workerQueue.take();
            if(stockQueueSet.checkRock(stock)){
                return;
            }
            stockQueueSet.pollStockOrder(stock);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
