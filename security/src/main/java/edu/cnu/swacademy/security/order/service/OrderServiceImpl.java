package edu.cnu.swacademy.security.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.cnu.swacademy.security.cashwallet.domain.CashWallet;
import edu.cnu.swacademy.security.cashwallet.domain.CashWalletHistory;
import edu.cnu.swacademy.security.cashwallet.repository.CashWalletRepository;
import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.common.Validate;
import edu.cnu.swacademy.security.common.dml.RepositoryMapper;
import edu.cnu.swacademy.security.common.dml.SaveCommitter;
import edu.cnu.swacademy.security.common.worker.CommitWorker;
import edu.cnu.swacademy.security.common.worker.Worker;
import edu.cnu.swacademy.security.market.domain.MarketStatus;
import edu.cnu.swacademy.security.market.repository.MarketStatusRepository;
import edu.cnu.swacademy.security.market.service.TickSizeUtil;
import edu.cnu.swacademy.security.order.domain.Match;
import edu.cnu.swacademy.security.order.domain.Order;
import edu.cnu.swacademy.security.order.domain.SideStatus;
import edu.cnu.swacademy.security.order.dto.*;
import edu.cnu.swacademy.security.order.dto.orderbook.*;
import edu.cnu.swacademy.security.order.repository.MatchRepository;
import edu.cnu.swacademy.security.order.repository.OrderRepository;
import edu.cnu.swacademy.security.stock.domain.Stock;
import edu.cnu.swacademy.security.stock.repository.StockRepository;
import edu.cnu.swacademy.security.stockwallet.domain.StockWallet;
import edu.cnu.swacademy.security.stockwallet.domain.StockWalletHistory;
import edu.cnu.swacademy.security.stockwallet.repository.StockWalletRepository;
import edu.cnu.swacademy.security.user.entity.User;
import edu.cnu.swacademy.security.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final Validate validate;
    private final UserRepository userRepository;
    private final CashWalletRepository cashWalletRepository;
    private final StockWalletRepository stockWalletRepository;
    private final StockRepository stockRepository;
    private final MarketStatusRepository marketStatusRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${exchange.server.port}")
    private int exchangeServerPort;
    @Value("${exchange.server.host}")
    private String exchangeServerHost;
    private final ApplicationContext applicationContext;
    private final MatchRepository matchRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public ExchangeDto order(int userId, OrderRequest orderRequest) throws SecurityException {
        Stock stock = validate.isStock(stockRepository.findById(orderRequest.getStockId()));
        MarketStatus stockStatus = marketStatusRepository.findTopByStockIdOrderByCreatedAtDesc(stock.getId());
        TickSizeUtil.valid(new BigDecimal(orderRequest.getPrice()), new BigDecimal(stockStatus.getReferencePrice()));
        User user = validate.isUser(userRepository.findById(userId));

        switch (SideStatus.valueOf(orderRequest.getSide())) {
            case BUY -> {
                return buyOrder(user, stock, orderRequest);
            }
            case SELL -> {
                return sellOrder(user, stock, orderRequest);
            }
            default -> throw new IllegalAccessError();
        }
    }

    @Override
    public void orderResult(ExchangeDto exchangeDto) throws SecurityException {
        if(exchangeDto.getMatchResult().equals("Matched")){
            Order takerOrder = validate.isOrder(orderRepository.findById(exchangeDto.getTakerOrderId()));
            Order makerOrder = validate.isOrder(orderRepository.findById(exchangeDto.getMakerOrderId()));
            switch (takerOrder.getSide()) {
                case BUY -> {
                    orderMatched(takerOrder,makerOrder,exchangeDto.getAmount());
                }
                case SELL -> {
                    orderMatched(makerOrder,takerOrder,exchangeDto.getAmount());
                }
                default -> throw new IllegalAccessError();
            }
        }else {
            orderReject();
        }
    }



    @Override
    public void orderCancel(int userId, int orderId) throws SecurityException {
        Order order  = validate.isOrder(orderRepository.findById(orderId));
        String url = String.format("http://%s:%s/api/v1/market/order", exchangeServerHost, exchangeServerPort);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ExchangeCancelRequest request = new ExchangeCancelRequest(orderId, order.getStock().getId());
        HttpEntity<ExchangeCancelRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<ExchangeDto> response =  restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                ExchangeDto.class
        );
        ExchangeDto exchangeRequest =  response.getBody();
        if(Objects.requireNonNull(exchangeRequest).getMatchResult().equals("Cancelled")){
            switch (order.getSide()) {
                case BUY -> {
                    buyCancelOrder(order);
                }
                case SELL -> {
                    sellCancelOrder(order);

                }
                default -> throw new IllegalAccessError();
            }
        }

    }

    @Override
    public OrderUnfilledsResponse getUnfilledOrders(int userId, int stockId, String side, int page, int size, String sort) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        Stock stock = validate.isStock(stockRepository.findById(stockId));
        Pageable pageable = sort.equals("desc") ? PageRequest.of(page, size, Sort.by("createdAt").descending()) :
                PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Order> orders = orderRepository.findByUserAndStockAndSide(pageable,user,stock,side);
        List<OrderUnfilledResponse> orderUnfilledResponses = new ArrayList<>();
        for (Order order: orders.getContent()) {
            orderUnfilledResponses.add(new OrderUnfilledResponse(order));
        }
        return new OrderUnfilledsResponse((int) orders.getTotalElements(),orderUnfilledResponses);
    }

    @Override
    public OrderFilledsResponse getMatchs(int userId, int stockId, String side, int page, int size, String sort) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        Stock stock = validate.isStock(stockRepository.findById(stockId));
        Pageable pageable = sort.equals("desc") ? PageRequest.of(page, size, Sort.by("createdAt").descending()) :
                PageRequest.of(page, size, Sort.by("createdAt").ascending());
        List<Match> matches = matchRepository.findByStock(stock);
        Set<Integer> orderSet = new HashSet<>();
        for (Match match : matches) {
            orderSet.add(match.getMakerOrder().getId());
            orderSet.add(match.getTakerOrder().getId());
        }
        Page<Order> orders = orderRepository.findByIdInAndUserAndStockAndSide(pageable,orderSet,user,stock,side);
        List<OrderFilledResponse> orderFilledResponses = new ArrayList<>();
        for (Order order: orders.getContent()) {
            orderFilledResponses.add(new OrderFilledResponse(order));
        }
        return new OrderFilledsResponse((int) orders.getTotalElements(),orderFilledResponses);
    }

    @Override
    public OrderBookResponse getOrderBook(int userId, int stockId) throws SecurityException {
        validate.isUser(userRepository.findById(userId));
        validate.isStock(stockRepository.findById(stockId));
        OrderBookBuy orderBookBuy = orderBookBuy(getOrderBook(stockId,"BUY"));
        OrderBookSell orderBookSell = orderBookSell(getOrderBook(stockId,"SELL")) ;
        return new OrderBookResponse(orderBookBuy,orderBookSell);
    }




    private void buyCancelOrder(Order order) throws SecurityException {
        int totalPrice = order.getPrice()*order.getAmount();
        order.cancel();
        CashWallet cashWallet = validate.isCashWallet(cashWalletRepository.findByUserId(order.getUser().getId()));
        validate.isBlock(cashWallet.isBlocked(),true);
        cashWallet.orderWithdraw(totalPrice);
        cashWallet.orderDeposit(totalPrice);
        CashWalletHistory cashWalletHistory = new CashWalletHistory("취소",totalPrice,"취소",cashWallet.getReserve(),cashWallet);
        Worker worker = new CommitWorker(new SaveCommitter(applicationContext.getBean(RepositoryMapper.class))
                ,new BaseEntity[]{order,cashWalletHistory});
        worker.execute();
    }

    private void sellCancelOrder(Order order) throws SecurityException {
        int totalAmount = order.getAmount();
        order.cancel();
        StockWallet stockWallet = validate.isStockWallet(stockWalletRepository.findByUserAndStock(order.getUser(),order.getStock()));
        validate.isBlock(stockWallet.isBlocked(),false);
        stockWallet.orderReserve(totalAmount);
        stockWallet.orderDeposit(totalAmount);
        StockWalletHistory StockWalletHistory = new StockWalletHistory("취소",totalAmount,"취소",stockWallet.getReserve(),stockWallet);
        Worker worker = new CommitWorker(new SaveCommitter(applicationContext.getBean(RepositoryMapper.class))
                ,new BaseEntity[]{order,StockWalletHistory});
        worker.execute();
    }


    private ExchangeDto sellOrder(User user, Stock stock, OrderRequest orderRequest) throws SecurityException {
        StockWallet stockWallet = validate.isStockWallet(stockWalletRepository.findByUserAndStock(user, stock));
        validate.isBlock(stockWallet.isBlocked(), false);
        validate.possibleOrder(stockWallet.getReserve(), orderRequest.getQuantity());

        stockWallet.order(orderRequest.getQuantity());
        stockWalletRepository.save(stockWallet);
        Order order = new Order(user, stock, SideStatus.SELL, orderRequest.getPrice(), orderRequest.getQuantity(), orderRequest.getQuantity());
        orderRepository.save(order);

        return exchange(new ExchangeRequest(order.getId(), order.getStock().getId(), order.getPrice(), order.getAmount(), "SELL", order.getCreatedAt()));

    }

    private ExchangeDto buyOrder(User user, Stock stock, OrderRequest orderRequest) throws SecurityException {
        CashWallet cashWallet = validate.isCashWallet(cashWalletRepository.findByUserId(user.getId()));
        validate.isBlock(cashWallet.isBlocked(), true);
        validate.possibleOrder(cashWallet.getReserve(), orderRequest.getPrice() * orderRequest.getQuantity());

        cashWallet.order(orderRequest.getPrice() * orderRequest.getQuantity());

        cashWalletRepository.save(cashWallet);

        Order order = new Order(user, stock, SideStatus.BUY, orderRequest.getPrice(), orderRequest.getQuantity(), orderRequest.getQuantity());
        orderRepository.save(order);

        return exchange(new ExchangeRequest(order.getId(), order.getStock().getId(), order.getPrice(), order.getAmount(), "BUY", order.getCreatedAt()));
    }

    private ExchangeDto exchange(ExchangeRequest exchangeRequest) {
        String url = String.format("http://%s:%s/api/v1/market/order", exchangeServerHost, exchangeServerPort);
        ResponseEntity<ExchangeDto> response =
                restTemplate.postForEntity(url, exchangeRequest, ExchangeDto.class);
        return response.getBody();
    }

    private void orderMatched(Order buyOrder, Order sellOrder, int amount) throws SecurityException {
        int buyTotalPrice = buyOrder.getPrice()*amount;
        CashWallet buyCashWallet = validate.isCashWallet(cashWalletRepository.findByUserId(buyOrder.getUser().getId()));
        CashWallet sellCashWallet = validate.isCashWallet(cashWalletRepository.findByUserId(sellOrder.getUser().getId()));

        validate.isBlock(buyCashWallet.isBlocked(),true);
        validate.isBlock(sellCashWallet.isBlocked(),true);

        buyCashWallet.orderWithdraw(buyTotalPrice);
        sellCashWallet.orderDeposit(buyTotalPrice);

        CashWalletHistory buyCashWalletHistory = new CashWalletHistory("거래대금지급",buyTotalPrice,"거래대금지급",buyCashWallet.getReserve(),buyCashWallet);
        CashWalletHistory sellCashWalletHistory =  new CashWalletHistory("거래대금수령",buyTotalPrice,"거래대금수령",sellCashWallet.getReserve(),sellCashWallet);

        StockWallet buyStockWallet = validate.isStockWallet(stockWalletRepository.findByUserAndStock(buyOrder.getUser(),buyOrder.getStock()));
        validate.isBlock(buyStockWallet.isBlocked(),false);
        buyStockWallet.orderReserve(amount);

        StockWalletHistory buyStockWalletHistory = new StockWalletHistory("구매주문체결",amount,"구매주문체결",buyStockWallet.getReserve(),buyStockWallet);

        StockWallet sellStockWallet = validate.isStockWallet(stockWalletRepository.findByUserAndStock(sellOrder.getUser(),sellOrder.getStock()));

        sellStockWallet.orderDeposit(amount);
        StockWalletHistory sellStockWalletHistory = new StockWalletHistory("판매주문체결",amount,"판매주문체결",sellStockWallet.getReserve(),sellStockWallet);

        buyOrder.minusAmount(amount);
        sellOrder.minusAmount(amount);

        MarketStatus stockStatus = marketStatusRepository.findTopByStockIdOrderByCreatedAtDesc(buyOrder.getStock().getId());

        stockStatus.addClosingPrice(buyOrder.getPrice());
        stockStatus.addHighestPrice(buyOrder.getPrice());
        stockStatus.addLowestPrice(buyOrder.getPrice());
        stockStatus.addOpeningPrice(buyOrder.getPrice());
        stockStatus.addTradingAmount(buyTotalPrice);
        stockStatus.addTradingVolume(amount);

        Match match = new Match(buyOrder.getStock(),sellOrder,buyOrder);

        Worker worker = new CommitWorker(new SaveCommitter(applicationContext.getBean(RepositoryMapper.class))
                ,new BaseEntity[]{buyCashWallet,sellCashWallet,buyCashWalletHistory,sellCashWalletHistory,buyStockWalletHistory,sellStockWalletHistory,buyOrder,sellOrder,stockStatus,match});
        worker.execute();

    }

    private List<PriceResponse> getOrderBook(int stockId,String Side) {
        Set<String> buyKeys = redisTemplate.keys(stockId + ":"+Side+":*");
        List<PriceResponse> responses = new ArrayList<>();

        for (String key : buyKeys) {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) continue;

            PriceResponse priceResponse;
            try {
                priceResponse = mapper.readValue(json, PriceResponse.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String[] parts = key.split(":");
            if (parts.length >= 3) {
                priceResponse.setPrice(parts[2]);
            }

            if (priceResponse.getOrders() == null) {
                priceResponse.setOrders(new ArrayList<>());
            }

            int totalQuantity = priceResponse.getOrders().stream()
                    .mapToInt(OrderInfoResponse::getUnfilled_quantity)
                    .sum();
            priceResponse.setTotal_quantity(new TotalQuantityResponse(totalQuantity));

            responses.add(priceResponse);
        }
        return responses;

    }
    private OrderBookSell orderBookSell(List<PriceResponse> responses) {
        OrderBookSell sell = new OrderBookSell();
        sell.setPrice(responses);
        return sell;
    }

    private OrderBookBuy orderBookBuy(List<PriceResponse> responses) {
        OrderBookBuy buy = new OrderBookBuy();
        buy.setPrice(responses);
        return buy;
    }
    private void orderReject() {
    }

}
