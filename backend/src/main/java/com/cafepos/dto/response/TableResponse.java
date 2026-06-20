package com.cafepos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableResponse {
    private Long id;
    private Long floorId;
    private String floorName;
    private Integer number;
    private Integer seats;
    private Boolean active;
    private String status; // computed: occupied | available
}
