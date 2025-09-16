package edu.cnu.swacademy.exchange.process;

import edu.cnu.swacademy.exchange.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class Process {
    public void orderProcess(Order order) { // 여기서 주문 처리할거임 일단 오더북만 보셈  그리고 나머지 작업 ( 잔액이라던가 잔고 라던가 )
                                            // 업데이트는 세이브하는 큐에 던져
    }
}
