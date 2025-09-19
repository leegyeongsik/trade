package edu.cnu.swacademy.security.market.service;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.market.dto.MarketResponse;

import java.io.IOException;
import java.math.BigDecimal;

public interface MarketService {
    MarketResponse openMarket() throws SecurityException, IOException;
    MarketResponse closeMarket() throws SecurityException;
}
