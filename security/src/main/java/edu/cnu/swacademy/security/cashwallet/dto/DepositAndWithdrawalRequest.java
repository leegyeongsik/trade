package edu.cnu.swacademy.security.cashwallet.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class DepositAndWithdrawalRequest {
    @Min(1)
    private int amount;
}
