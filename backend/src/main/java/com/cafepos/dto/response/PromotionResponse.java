package com.cafepos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionResponse {
    private Long id;
    private String name;
    private String applyScope;
    private Long productId;
    private String productName;
    private Integer minQty;
    private BigDecimal minOrderAmount;
    private String discountType;
    private BigDecimal value;
    private Boolean active;
}
