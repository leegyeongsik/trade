package edu.cnu.swacademy.exchange.controller;

import edu.cnu.swacademy.exchange.dto.ExchangeResponse;
import edu.cnu.swacademy.exchange.dto.OrderDeleteRequest;
import edu.cnu.swacademy.exchange.dto.OrderRequest;
import edu.cnu.swacademy.exchange.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController("/api/v1/market/order")
public class ExchangeController {
    private final ExchangeService exchangeService;
    @PostMapping
    public ExchangeResponse orderProcess(@RequestBody OrderRequest orderRequest){
        return exchangeService.orderProcess(orderRequest);
    }
    @DeleteMapping
    public ExchangeResponse orderDeleteProcess(@RequestBody OrderDeleteRequest orderDeleteRequest){
        return exchangeService.orderDeleteProcess(orderDeleteRequest);
    }


}
