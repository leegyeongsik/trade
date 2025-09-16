package edu.cnu.swacademy.exchange.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderRequest {
    @Min(1)
    int stockId;

    @NotBlank
    @Pattern(regexp = "BUY|SELL")
    String side;

    @Min(1)
    int price;

    @Min(1)
    int quantity;
}
