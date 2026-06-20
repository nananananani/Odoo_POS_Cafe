package com.cafepos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequest {
    @NotBlank(message = "Payment method name is required")
    private String name;

    @NotBlank(message = "Payment method type is required")
    @Pattern(regexp = "(?i)CASH|CARD|UPI", message = "Type must be CASH, CARD, or UPI")
    private String type;

    private String upiId;

    private Boolean active = true;
}
