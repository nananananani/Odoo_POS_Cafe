package com.cafepos.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @NotBlank(message = "UOM is required")
    private String uom;

    @NotNull(message = "Tax percentage is required")
    @Min(value = 0, message = "Tax percentage cannot be less than 0")
    @Max(value = 100, message = "Tax percentage cannot be more than 100")
    private BigDecimal taxPct;

    private String description;

    private Boolean showOnKds = true;

    private Boolean active = true;
}
