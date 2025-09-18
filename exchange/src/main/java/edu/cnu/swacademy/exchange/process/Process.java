package edu.cnu.swacademy.exchange.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cnu.swacademy.exchange.domain.Order;
import edu.cnu.swacademy.exchange.dto.ExchangeResponse;
import edu.cnu.swacademy.exchange.dto.OrderStatus;
import edu.cnu.swacademy.exchange.dto.RedisOrderDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class Process {
    private final OrderComplete orderComplete;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private static final String BUY = "BUY";
    private static final String SELL = "SELL";

    public Process(OrderComplete orderComplete, StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.orderComplete = orderComplete;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public void orderProcess(Order order) {
        process(order, order.getSide().equals(BUY));
    }

    private void process(Order order, boolean isBuy) {
        int currentAmount = order.getAmount();
        String key = createKey(order.getProduct_id(), !isBuy, order.getPrice());

        while (currentAmount > 0) {
            String orderJson = getOrder(key);
            if (orderJson == null) break;

            RedisOrderDto sellOrder;
            try {
                sellOrder = objectMapper.readValue(orderJson, RedisOrderDto.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (sellOrder.getUnfilledUnit() == 0) {
                stringRedisTemplate.opsForList().leftPop(key);
                continue;
            }
            int sellAmount = sellOrder.getUnfilledUnit();
            int matchedAmount = Math.min(currentAmount, sellAmount);

            sellOrder.setUnfilledUnit(sellAmount - matchedAmount);
            currentAmount -= matchedAmount;

            complete(new ExchangeResponse(
                    OrderStatus.Matched.name(),
                    order.getOrder_id(),
                    sellOrder.getOrderId(),
                    matchedAmount
            ));
            stringRedisTemplate.opsForList().leftPop(key);
            unfillOrderCreate(sellOrder, !isBuy, order.getProduct_id(), order.getPrice());
        }
        unfillOrderCreate(new RedisOrderDto(order.getOrder_id(),
                        order.getCreated_at(),
                        currentAmount),
                isBuy,
                order.getProduct_id(),
                order.getPrice());
    }

    private String getOrder(String key) {
        return stringRedisTemplate.opsForList().index(key, 0);
    }

    private void complete(ExchangeResponse exchangeResponse) {
        orderComplete.offer(exchangeResponse);
    }

    private String createKey(int productId, boolean isBuy, int price) {
        return isBuy ?
                String.format("%d:%s:%d", productId, BUY, price) :
                String.format("%d:%s:%d", productId, SELL, price);
    }

    private void unfillOrderCreate(RedisOrderDto order, boolean isBuy, int productId, int price) {
        if (order.getUnfilledUnit() != 0) {
            try {
                stringRedisTemplate.opsForList().rightPush(createKey(productId, isBuy, price), objectMapper.writeValueAsString(order));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
