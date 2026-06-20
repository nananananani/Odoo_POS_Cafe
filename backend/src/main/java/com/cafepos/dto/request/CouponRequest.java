package com.cafepos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequest {
    @NotBlank(message = "Coupon name is required")
    private String name;

    @NotBlank(message = "Coupon code is required")
    private String code;

    @NotBlank(message = "Discount type is required")
    @Pattern(regexp = "(?i)PERCENT|FIXED", message = "Discount type must be PERCENT or FIXED")
    private String discountType;

    @NotNull(message = "Discount value is required")
    @Positive(message = "Discount value must be greater than zero")
    private BigDecimal value;

    @NotNull(message = "Active status is required")
    private Boolean active;
}
