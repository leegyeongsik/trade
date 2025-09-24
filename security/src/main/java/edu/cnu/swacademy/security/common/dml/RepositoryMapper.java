package edu.cnu.swacademy.security.common.dml;

import edu.cnu.swacademy.security.cashwallet.domain.CashWallet;
import edu.cnu.swacademy.security.order.domain.Order;
import edu.cnu.swacademy.security.cashwallet.domain.CashWalletHistory;
import edu.cnu.swacademy.security.cashwallet.repository.CashWalletHistoryRepository;
import edu.cnu.swacademy.security.cashwallet.repository.CashWalletRepository;
import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.market.domain.MarketStatus;
import edu.cnu.swacademy.security.market.repository.MarketStatusRepository;
import edu.cnu.swacademy.security.order.domain.Match;
import edu.cnu.swacademy.security.order.repository.MatchRepository;
import edu.cnu.swacademy.security.order.repository.OrderRepository;
import edu.cnu.swacademy.security.stock.domain.Stock;
import edu.cnu.swacademy.security.stock.repository.StockRepository;
import edu.cnu.swacademy.security.stockwallet.domain.StockWallet;
import edu.cnu.swacademy.security.stockwallet.domain.StockWalletHistory;
import edu.cnu.swacademy.security.stockwallet.repository.StockWalletHistoryRepository;
import edu.cnu.swacademy.security.stockwallet.repository.StockWalletRepository;
import edu.cnu.swacademy.security.user.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RepositoryMapper { // 저렇게 넣기싫으면 아예 다른데로 빼서 거기서 생성자에 넣고 그거 저따가 주입받어
    HashMap<Class<?>, JpaRepository<?,?>> repositoryHashMap = new HashMap<>();
    public RepositoryMapper(CashWalletHistoryRepository cashWalletHistoryRepository ,
                            StockWalletHistoryRepository stockWalletHistoryRepository,
                            UserRepository userRepository,
                            StockRepository stockRepository,
                            MatchRepository matchRepository,
                            MarketStatusRepository marketStatusRepository,
                            OrderRepository orderRepository,
                            CashWalletRepository cashWalletRepository,
                            StockWalletRepository stockWalletRepository){
        repositoryHashMap.put(CashWalletHistory.class, cashWalletHistoryRepository);
        repositoryHashMap.put(StockWalletHistory.class, stockWalletHistoryRepository);
        repositoryHashMap.put(User.class,userRepository);
        repositoryHashMap.put(Stock.class,stockRepository);
        repositoryHashMap.put(Match.class,matchRepository);
        repositoryHashMap.put(MarketStatus.class,marketStatusRepository);
        repositoryHashMap.put(Order.class,orderRepository);
        repositoryHashMap.put(CashWallet.class,cashWalletRepository);
        repositoryHashMap.put(StockWallet.class,stockWalletRepository);
    }

    public <T extends BaseEntity, ID> JpaRepository<T, ID> getRepository(Class<T> clazz) {
        return (JpaRepository<T, ID>) repositoryHashMap.get(clazz);
    }
}
