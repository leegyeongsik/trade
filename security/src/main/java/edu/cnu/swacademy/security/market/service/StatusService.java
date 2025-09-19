package edu.cnu.swacademy.security.market.service;

import edu.cnu.swacademy.security.common.SecurityException;

import java.math.BigDecimal;

public interface StatusService {
    void createMarketStatus() throws SecurityException;
    public BigDecimal calculateUpperLimitPrice(BigDecimal referencePrice);
    public BigDecimal calculateLowerLimitPrice(BigDecimal referencePrice);

}
