package com.cafepos.dto.response;

import com.cafepos.entity.UserRole;
import com.cafepos.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAt;
}
