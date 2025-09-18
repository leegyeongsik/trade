package edu.cnu.swacademy.exchange.config;

import edu.cnu.swacademy.exchange.domain.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class StockMapSetting {
    private final StringRedisTemplate redisTemplate;

    public StockMapSetting(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Object[] createMap() {
        ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Order>> stockQueueMap = new ConcurrentHashMap<>(); // stockId : queue
        ConcurrentHashMap<Integer, ReentrantLock> stockLockMap = new ConcurrentHashMap<>();
        Set<String> stockKeys = redisTemplate.opsForSet().members("allStockIds");
        for (String stockKey : Objects.requireNonNull(stockKeys)) {
            int stockId = Integer.parseInt(stockKey);
            stockQueueMap.put(stockId, new ConcurrentLinkedQueue<>());
            stockLockMap.put(stockId,new ReentrantLock());
        }
        return new Object[]{stockQueueMap, stockLockMap};
    }
}
