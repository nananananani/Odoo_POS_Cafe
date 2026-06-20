package com.cafepos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@jakarta.persistence.Table(name = "table_qr_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableQrToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @Column(nullable = false, unique = true)
    private String token;
}
