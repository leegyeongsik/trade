package edu.cnu.swacademy.exchange.service;

import edu.cnu.swacademy.exchange.dto.ExchangeResponse;
import edu.cnu.swacademy.exchange.dto.OrderDeleteRequest;
import edu.cnu.swacademy.exchange.dto.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceImpl implements ExchangeService{
    @Override
    public ExchangeResponse orderProcess(OrderRequest orderRequest) {
        return null;
        // 받아서 종목 큐에 집어넣음
        // 스레드풀이 그거 꺼내서 처리

        // 처음에 로딩될떄
        // 오더북을 세팅을 하고 종목으로
        //
    }

    @Override
    public ExchangeResponse orderDeleteProcess(OrderDeleteRequest orderDeleteRequest) {
        return null;
        // 바로 처리

    }
}
