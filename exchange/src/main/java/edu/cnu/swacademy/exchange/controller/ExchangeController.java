package edu.cnu.swacademy.exchange.controller;

import edu.cnu.swacademy.exchange.dto.ExchangeResponse;
import edu.cnu.swacademy.exchange.dto.OrderDeleteRequest;
import edu.cnu.swacademy.exchange.dto.OrderRequest;
import edu.cnu.swacademy.exchange.service.ExchangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/v1/market/order")
public class ExchangeController {
    private final ExchangeService exchangeService;
    @PostMapping
    public ExchangeResponse orderProcess(@Valid @RequestBody OrderRequest orderRequest){
        return exchangeService.orderProcess(orderRequest);
    }

    @DeleteMapping
    public ExchangeResponse orderDeleteProcess(@RequestBody OrderDeleteRequest orderDeleteRequest){
        return exchangeService.orderDeleteProcess(orderDeleteRequest);
    }
}
