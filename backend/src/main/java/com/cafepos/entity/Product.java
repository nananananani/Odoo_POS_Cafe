package com.cafepos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@jakarta.persistence.Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String uom;

    @Column(name = "tax_pct", nullable = false)
    private BigDecimal taxPct;

    private String description;

    @Column(name = "show_on_kds", nullable = false)
    private Boolean showOnKds;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
