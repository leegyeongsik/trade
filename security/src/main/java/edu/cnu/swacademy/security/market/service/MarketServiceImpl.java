package edu.cnu.swacademy.security.market.service;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.common.Validate;
import edu.cnu.swacademy.security.market.domain.EngineStatus;
import edu.cnu.swacademy.security.market.domain.MarketStatus;
import edu.cnu.swacademy.security.market.dto.MarketResponse;
import edu.cnu.swacademy.security.market.repository.MarketStatusRepository;
import edu.cnu.swacademy.security.stock.domain.Stock;
import edu.cnu.swacademy.security.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private final MarketStatusRepository marketStatusRepository;
    private final StockRepository stockRepository;
    private static final int TEMPORARY_PREVIOUS_CLOSE_PRICE = 30000;
    private final StringRedisTemplate redisTemplate;

    @Override
    public MarketResponse openMarket() throws SecurityException, IOException {
        validate.exchangeServerNotExistCheck(exchangeServerProcess != null && exchangeServerProcess.isAlive());
        List<MarketStatus> latestMarketStatus = marketStatusRepository.findLatestMarketStatusPerStock();
        if (latestMarketStatus.isEmpty()) {
            List<Stock> stocks = stockRepository.findAll();
            for (Stock stock : stocks) {
                latestMarketStatus.add(new MarketStatus(stock, LocalDateTime.now(), TEMPORARY_PREVIOUS_CLOSE_PRICE,
                        statusService.calculateUpperLimitPrice(BigDecimal.valueOf(TEMPORARY_PREVIOUS_CLOSE_PRICE)).intValue(),
                        statusService.calculateLowerLimitPrice(BigDecimal.valueOf(TEMPORARY_PREVIOUS_CLOSE_PRICE)).intValue()));
            }
            marketStatusRepository.saveAll(latestMarketStatus);
        }
        for (MarketStatus marketStatus : latestMarketStatus) {
            redisTemplate.opsForSet().add("allStockIds", String.valueOf(marketStatus.getStock().getId()));
        }
        redisTemplate.expire("allStockIds", 5, TimeUnit.MINUTES);
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
