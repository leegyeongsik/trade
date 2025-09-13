package edu.cnu.swacademy.security.stock.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class StockReserveRequest {
    @Min(1)
    int stockWalletId;
    @Min(1)
    int amount;
}
