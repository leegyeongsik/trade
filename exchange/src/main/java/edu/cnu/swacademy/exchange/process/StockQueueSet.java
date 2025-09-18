package edu.cnu.swacademy.exchange.process;

import edu.cnu.swacademy.exchange.config.StockMapSetting;
import edu.cnu.swacademy.exchange.domain.Order;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Component
@RequiredArgsConstructor
@DependsOn("stockMapSetting")
public class StockQueueSet {
    private final StockMapSetting stockMapSetting;
    ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Order>> stockQueueMap;
    ConcurrentHashMap<Integer, ReentrantLock> stockLockMap;
    private final Process process;
    @PostConstruct
    public void init() {
        Object[] maps =  stockMapSetting.createMap();
        stockQueueMap = (ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Order>>) maps[0];
        stockLockMap = (ConcurrentHashMap<Integer, ReentrantLock>) maps[1];
    }

    void offerStockOrder(int stock, Order order) {
        ConcurrentLinkedQueue<Order> queue = stockQueueMap.get(stock);
        queue.offer(order);
    }

    void pollStockOrder(int stock) {
        Lock lock = stockLockMap.get(stock); // 같은 스톡으로 큐에 접근할건데 저기서 먼저 락을 쥐었으면
        try {                               // 다른 스레드가 같은 스톡의 lock을 접근했을때 락이 없어서 못들어감
            lock.lock();                    // 결과적으로 큐에 못들어감 락이 없어서
            ConcurrentLinkedQueue<Order> queue = stockQueueMap.get(stock);
            if (queue.isEmpty()) {
                return;
            }
            while (!queue.isEmpty()) {
                process.orderProcess(queue.poll());
            }
        } finally {
            lock.unlock();
        }
    }

    boolean checkRock(int stock) {
        return stockLockMap.get(stock).isLocked();
    }

}
