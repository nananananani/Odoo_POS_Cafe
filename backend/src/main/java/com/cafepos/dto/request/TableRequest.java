package com.cafepos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableRequest {
    @NotNull(message = "Floor ID is required")
    private Long floorId;

    @NotNull(message = "Table number is required")
    @Min(value = 1, message = "Table number must be at least 1")
    private Integer number;

    @NotNull(message = "Seats count is required")
    @Min(value = 1, message = "Seats count must be at least 1")
    private Integer seats;

    private Boolean active = true;
}
