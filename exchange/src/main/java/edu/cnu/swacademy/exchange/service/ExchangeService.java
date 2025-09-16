package edu.cnu.swacademy.exchange.service;


import edu.cnu.swacademy.exchange.dto.ExchangeResponse;
import edu.cnu.swacademy.exchange.dto.OrderDeleteRequest;
import edu.cnu.swacademy.exchange.dto.OrderRequest;

public interface ExchangeService {

    ExchangeResponse orderProcess(OrderRequest orderRequest);

    ExchangeResponse orderDeleteProcess(OrderDeleteRequest orderDeleteRequest);
}
