package com.cafepos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkActionRequest {
    @NotEmpty(message = "Product IDs cannot be empty")
    private List<Long> ids;

    @NotBlank(message = "Action is required")
    @Pattern(regexp = "delete|archive", message = "Action must be either delete or archive")
    private String action;
}
