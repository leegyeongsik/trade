package edu.cnu.swacademy.exchange.process;

import edu.cnu.swacademy.exchange.domain.Order;
import edu.cnu.swacademy.exchange.domain.Stock;
import lombok.RequiredArgsConstructor;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
@RequiredArgsConstructor
public class StockQueueSet {
    // 큐를 모아놓고 각 큐 진입전에 락 걸거임 락걸고 처리하고 락 품

    ConcurrentHashMap<Stock, ConcurrentLinkedQueue<Order>> stockQueueMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<Stock, Lock> stockLockMap = new ConcurrentHashMap<>();
    private final Process process;

        void offerStockOrder(Stock stock,Order order){
           Queue<Order> queue =   stockQueueMap.get(stock);
           queue.offer(order);
        }
        void pollStockOrder(Stock stock){
            Lock lock = stockLockMap.get(stock); // 같은 스톡으로 큐에 접근할건데 저기서 먼저 락을 쥐었으면
            try {                               // 다른 스레드가 같은 스톡의 lock을 접근했을때 락이 없어서 못들어감
                lock.lock();                    // 결과적으로 큐에 못들어감 락이 없어서
                Queue<Order> queue =   stockQueueMap.get(stock);
                if (queue.isEmpty()) {
                    return;
                }
                process.orderProcess(queue.poll());
            }finally {
                lock.unlock();
            }
        }
}
