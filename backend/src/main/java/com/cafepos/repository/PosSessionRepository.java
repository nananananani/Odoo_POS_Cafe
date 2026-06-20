package com.cafepos.repository;

import com.cafepos.entity.PosSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosSessionRepository extends JpaRepository<PosSession, Long> {
}
