package com.cafepos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "self_order_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelfOrderConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SelfOrderMode mode;

    @Column(name = "bgColor")
    private String bgColor;

    @Convert(converter = StringListConverter.class)
    @Column(name = "bgImages")
    private List<String> bgImages;
}
