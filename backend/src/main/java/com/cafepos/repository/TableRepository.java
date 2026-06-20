package com.cafepos.repository;

import com.cafepos.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    boolean existsByFloorIdAndNumber(Long floorId, Integer number);
    boolean existsByFloorIdAndNumberAndIdNot(Long floorId, Integer number, Long id);
    boolean existsByFloorId(Long floorId);
}
