package edu.cnu.swacademy.security.cashwallet.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class DepositAndWithdrawalRequest {
    @Min(1)
    int amount;
}
