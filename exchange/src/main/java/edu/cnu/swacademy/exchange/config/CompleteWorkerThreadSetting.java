package edu.cnu.swacademy.exchange.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Getter
@Component
public class CompleteWorkerThreadSetting {
    @Value("${complete.worker.size}")
    private int totalThread;
}
