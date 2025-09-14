package edu.cnu.swacademy.security.market.controller;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.market.dto.MarketResponse;
import edu.cnu.swacademy.security.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController("/api/v1/market/")

public class MarketController {
    private final MarketService marketService;

    @PostMapping("/open")
    MarketResponse openMarket() throws SecurityException, IOException {
        return marketService.openMarket();
    }
    @PostMapping("/close")
    MarketResponse closeMarket() throws SecurityException {
        return marketService.closeMarket();
    }

}
