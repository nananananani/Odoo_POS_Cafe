package com.cafepos.repository;

import com.cafepos.entity.TableQrToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TableQrTokenRepository extends JpaRepository<TableQrToken, Long> {
    Optional<TableQrToken> findByToken(String token);
}
