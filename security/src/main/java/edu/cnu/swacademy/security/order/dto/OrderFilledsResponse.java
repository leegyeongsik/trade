package edu.cnu.swacademy.security.order.dto;

import java.util.List;

public record OrderFilledsResponse(int total_elements, List<OrderFilledResponse> rows) {
}
