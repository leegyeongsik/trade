package edu.cnu.swacademy.exchange.common;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements ApplicationRunner {


    public Initializer() {
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("=== OrderBook 초기화 완료 ===");
    }
}