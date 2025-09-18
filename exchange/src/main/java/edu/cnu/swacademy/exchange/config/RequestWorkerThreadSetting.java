package edu.cnu.swacademy.exchange.config;

import edu.cnu.swacademy.exchange.process.StockQueueSet;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
@Getter
@Component
@DependsOn("stockQueueSet")
public class RequestWorkerThreadSetting {
    private final ApplicationContext applicationContext;
    private int totalThread;
    public RequestWorkerThreadSetting(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    @PostConstruct
    public void init() {
        StockQueueSet stockQueueSet = applicationContext.getBean(StockQueueSet.class);
        totalThread = stockQueueSet.getStockQueueMap().size();
    }
}
