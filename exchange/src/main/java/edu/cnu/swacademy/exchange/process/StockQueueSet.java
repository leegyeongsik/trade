package edu.cnu.swacademy.exchange.process;

import edu.cnu.swacademy.exchange.domain.Order;
import edu.cnu.swacademy.exchange.domain.Stock;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;

public class StockQueueSet {
    // 큐를 모아놓고 각 큐 진입전에 락 걸거임 락걸고 처리하고 락 품

    Map<Stock, Queue<Order>> stockQueueMap = new HashMap<>();
    Map<Stock, Lock> stockLockMap = new HashMap<>();

    void offerStockOrder(Stock stock,Order order){
       Queue<Order> queue =   stockQueueMap.get(stock);
       queue.offer(order);
    }
    void pollStockOrder(Stock stock){
        Lock lock = stockLockMap.get(stock);
        try {
            lock.lock();
            Queue<Order> queue =   stockQueueMap.get(stock);
            if (queue.isEmpty()) {
                return;
            }
             queue.poll();
        }finally {
            lock.unlock();
        }
    }
}
