package edu.cnu.swacademy.security.market.controller;

import edu.cnu.swacademy.security.market.dto.MarketResponse;
import edu.cnu.swacademy.security.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController("/api/v1/market/")

public class MarketController {
    private final MarketService marketService;

    @PostMapping("/open")
    MarketResponse openMarket(){
        return marketService.openMarket();
    }
    @PostMapping("/close")
    MarketResponse closeMarket(){
        return marketService.closeMarket();
    }

}
