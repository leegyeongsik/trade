package edu.cnu.swacademy.security.market.service;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.common.Validate;
import edu.cnu.swacademy.security.market.domain.EngineStatus;
import edu.cnu.swacademy.security.market.dto.MarketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {
    @Value("${exchange.server.jar-path}")
    private String exchangeServerJarPath;
    private Process exchangeServerProcess;

    @Value("${exchange.server.port}")
    private int exchangeServerPort;

    @Value("${exchange.server.host}")
    private String exchangeServerHost;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;
    private final Validate validate;
    private final StatusService statusService;

    @Override
    public MarketResponse openMarket() throws SecurityException, IOException {
        validate.exchangeServerNotExistCheck(exchangeServerProcess != null && exchangeServerProcess.isAlive());

        startExchangeServer();
        return new MarketResponse(EngineStatus.RUNNING, LocalDateTime.now());
    }

    @Override
    public MarketResponse closeMarket() throws SecurityException {
        validate.exchangeServerExistCheck(exchangeServerProcess != null && exchangeServerProcess.isAlive());

        shutdownExchangeServer();

        statusService.createMarketStatus();
        return new MarketResponse(EngineStatus.STOPPED, LocalDateTime.now());
    }

    private void startExchangeServer() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-jar",
                exchangeServerJarPath,
                "--server.port=" + exchangeServerPort,
                "--server.address=" + exchangeServerHost
        );
        pb.environment().put("REDIS_HOST", redisHost);
        pb.environment().put("REDIS_PORT", String.valueOf(redisPort));
        pb.inheritIO();
        this.exchangeServerProcess = pb.start();
    }

    private void shutdownExchangeServer() {
        exchangeServerProcess.destroy();
        while (exchangeServerProcess.isAlive()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        exchangeServerProcess = null;
    }

}
