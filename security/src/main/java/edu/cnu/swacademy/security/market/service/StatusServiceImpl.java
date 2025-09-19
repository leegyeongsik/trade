package edu.cnu.swacademy.security.market.service;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.common.Validate;
import edu.cnu.swacademy.security.market.domain.MarketStatus;
import edu.cnu.swacademy.security.market.repository.MarketStatusRepository;
import edu.cnu.swacademy.security.stock.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService{
    private final MarketStatusRepository marketStatusRepository;
    private static final int TEMPORARY_PREVIOUS_CLOSE_PRICE = 30000;
    private static final BigDecimal UPPER = new BigDecimal("1.05");
    private static final BigDecimal LOWER = new BigDecimal("0.95");
    private final Validate validate;

    @Transactional(rollbackFor = Exception.class)
    public void createMarketStatus() throws SecurityException {
        List<MarketStatus> latestMarketStatus = marketStatusRepository.findLatestMarketStatusPerStock();
        validate.isMarketStatus(latestMarketStatus);

        List<MarketStatus> nextStatuses = new ArrayList<>();
        for (MarketStatus todayStatus : latestMarketStatus) {
            LocalDateTime nextStartOfDay = LocalDate.now().plusDays(1).atStartOfDay();
            nextStatuses.add(createStatus(todayStatus.getStock(), todayStatus,nextStartOfDay));
        }
        marketStatusRepository.saveAll(nextStatuses);
    }
    public MarketStatus createStatus(Stock stock,MarketStatus todayStatus, LocalDateTime nextStartOfDay) {
        BigDecimal referencePrice = todayStatus.getTradingVolume() != 0
                ? BigDecimal.valueOf(todayStatus.getTradingAmount())
                .divide(BigDecimal.valueOf(todayStatus.getTradingVolume()), 0, RoundingMode.HALF_UP)
                : BigDecimal.valueOf(TEMPORARY_PREVIOUS_CLOSE_PRICE);

        int upperLimitPrice = calculateUpperLimitPrice(referencePrice).intValue();
        int lowerLimitPrice = calculateLowerLimitPrice(referencePrice).intValue();

        return new MarketStatus(stock,nextStartOfDay, referencePrice.intValue(),upperLimitPrice,lowerLimitPrice);
    }

    public BigDecimal calculateUpperLimitPrice(BigDecimal referencePrice) {
        return TickSizeUtil.validateAndAdjustTickSize(referencePrice.multiply(UPPER));
    }

    public BigDecimal calculateLowerLimitPrice(BigDecimal referencePrice) {
        return TickSizeUtil.validateAndAdjustTickSize(referencePrice.multiply(LOWER));
    }
}
