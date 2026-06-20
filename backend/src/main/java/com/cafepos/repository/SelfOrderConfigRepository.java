package com.cafepos.repository;

import com.cafepos.entity.SelfOrderConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfOrderConfigRepository extends JpaRepository<SelfOrderConfig, Long> {
}
