package edu.cnu.swacademy.security.common.dml;

import edu.cnu.swacademy.security.cashwallet.domain.CashWalletHistory;
import edu.cnu.swacademy.security.cashwallet.repository.CashWalletHistoryRepository;
import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.stock.domain.StockWalletHistory;
import edu.cnu.swacademy.security.stock.repository.StockWalletHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RepositoryMapper { // 저렇게 넣기싫으면 아예 다른데로 빼서 거기서 생성자에 넣고 그거 저따가 주입받어
    HashMap<Class<?>, JpaRepository<?,?>> repositoryHashMap = new HashMap<>();
    public RepositoryMapper(CashWalletHistoryRepository cashWalletHistoryRepository , StockWalletHistoryRepository stockWalletHistoryRepository){
        repositoryHashMap.put(CashWalletHistory.class, cashWalletHistoryRepository);
        repositoryHashMap.put(StockWalletHistory.class, stockWalletHistoryRepository);
    }

    public <T extends BaseEntity, ID> JpaRepository<T, ID> getRepository(Class<T> clazz) {
        return (JpaRepository<T, ID>) repositoryHashMap.get(clazz);
    }
}
