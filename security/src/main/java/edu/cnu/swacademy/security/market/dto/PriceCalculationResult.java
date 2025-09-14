package edu.cnu.swacademy.security.market.dto;

import java.math.BigDecimal;

public record PriceCalculationResult(
        BigDecimal referencePrice,    // 기준가 (전일 거래량가중평균가격)
        BigDecimal upperLimitPrice,   // 상한가 (기준가 × 1.05)
        BigDecimal lowerLimitPrice    // 하한가 (기준가 × 0.95)
) {
}