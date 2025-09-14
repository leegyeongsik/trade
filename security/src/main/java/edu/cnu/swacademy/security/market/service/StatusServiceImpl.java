package edu.cnu.swacademy.security.market.service;

import edu.cnu.swacademy.security.market.domain.MarketStatus;
import edu.cnu.swacademy.security.market.repository.MarketStatusRepository;
import edu.cnu.swacademy.security.stock.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService{
    private final MarketStatusRepository marketStatusRepository;

    @Override
    public void createStatus() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        LocalDateTime nextStartOfDay = startOfDay.plusDays(1);
        List<MarketStatus> todayStatuses = marketStatusRepository.findAllByCreatedAtBetween(startOfDay, endOfDay); // 이거 스레드로 뺄수있으면 빼자 만들고 save에 넣으면 되니까
        List<MarketStatus> nextStatuses = new ArrayList<>();                                                       // 그럼 만드는 부분을 스레드가 만들면 되지
        for (MarketStatus todayStatus : todayStatuses) {                                                            // 그러면 saveall도 하나 추가
            nextStatuses.add(checkVolume(todayStatus.getStock(), todayStatus,nextStartOfDay));
        }
        marketStatusRepository.saveAll(nextStatuses);
    }

    private MarketStatus checkVolume(Stock stock,MarketStatus todayStatus, LocalDateTime nextStartOfDay) {
        if(todayStatus.getTradingAmount() == 0){
            return zero(stock,todayStatus,nextStartOfDay);
        }else {
            return notZero(stock,todayStatus,nextStartOfDay);
        }
    }

    private MarketStatus notZero(Stock stock, MarketStatus todayStatus, LocalDateTime nextStartOfDay) {
        int referencePrice = (int) (todayStatus.getTradingAmount() / todayStatus.getTradingVolume());
        int upperLimitPrice = (int) Math.round(referencePrice * 1.3);
        int lowerLimitPrice = (int) Math.round(referencePrice * 0.7);
        return new MarketStatus(stock,nextStartOfDay, referencePrice,upperLimitPrice,lowerLimitPrice);
    }

    private MarketStatus zero(Stock stock, MarketStatus todayStatus, LocalDateTime nextStartOfDay) {
        return new MarketStatus(stock,nextStartOfDay, todayStatus.getReferencePrice(),todayStatus.getUpperLimitPrice(),todayStatus.getLowerLimitPrice());
    }
}
