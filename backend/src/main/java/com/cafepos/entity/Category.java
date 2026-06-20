package com.cafepos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Convert(converter = StringListConverter.class)
    @Column(nullable = false)
    private List<String> colors;
}
