package com.cafepos.repository;

import com.cafepos.entity.Order;
import com.cafepos.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findBySessionId(Long sessionId);
    List<Order> findByTableIdAndStatus(Long tableId, OrderStatus status);
}
