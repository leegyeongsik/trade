package edu.cnu.swacademy.security.market.dto;

import edu.cnu.swacademy.security.market.domain.EngineStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class MarketResponse {
    String engine_status;
    String opened_at;
    public MarketResponse(EngineStatus engine_status , LocalDateTime openedAt){
        this.engine_status = engine_status.name();
        this.opened_at = openedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
