package com.cafepos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private List<String> categoryColors;
    private BigDecimal price;
    private String uom;
    private BigDecimal taxPct;
    private String description;
    private Boolean showOnKds;
    private Boolean active;
}
