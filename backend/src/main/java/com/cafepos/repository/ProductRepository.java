package com.cafepos.repository;

import com.cafepos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.category c WHERE " +
           "(:categoryId IS NULL OR c.id = :categoryId) AND " +
           "(:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Product> searchProducts(@Param("categoryId") Long categoryId, @Param("search") String search);

    boolean existsByCategoryId(Long categoryId);
}
