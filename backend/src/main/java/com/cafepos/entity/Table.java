package com.cafepos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@jakarta.persistence.Table(name = "pos_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private Integer seats;

    @Column(nullable = false)
    private Boolean active;

    @Transient
    private String status; // derived status [available|occupied]
}
