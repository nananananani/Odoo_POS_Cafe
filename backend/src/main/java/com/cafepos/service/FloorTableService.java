package com.cafepos.service;

import com.cafepos.dto.request.FloorRequest;
import com.cafepos.dto.request.TableRequest;
import com.cafepos.dto.response.FloorResponse;
import com.cafepos.dto.response.TableResponse;
import com.cafepos.entity.Floor;
import com.cafepos.entity.OrderStatus;
import com.cafepos.entity.Table;
import com.cafepos.exception.NotFoundException;
import com.cafepos.exception.ValidationException;
import com.cafepos.repository.FloorRepository;
import com.cafepos.repository.OrderRepository;
import com.cafepos.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.cafepos.exception.ConflictException;

@Service
@RequiredArgsConstructor
public class FloorTableService {

    private final FloorRepository floorRepository;
    private final TableRepository tableRepository;
    private final OrderRepository orderRepository;

    // --- Floor CRUD ---

    @Transactional(readOnly = true)
    public List<FloorResponse> getAllFloors() {
        return floorRepository.findAll().stream()
                .map(this::mapToFloorResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FloorResponse getFloorById(Long id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Floor not found with id: " + id));
        return mapToFloorResponse(floor);
    }

    @Transactional
    public FloorResponse createFloor(FloorRequest request) {
        Floor floor = Floor.builder()
                .name(request.getName())
                .build();
        Floor savedFloor = floorRepository.save(floor);
        return mapToFloorResponse(savedFloor);
    }

    @Transactional
    public FloorResponse updateFloor(Long id, FloorRequest request) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Floor not found with id: " + id));
        floor.setName(request.getName());
        Floor updatedFloor = floorRepository.save(floor);
        return mapToFloorResponse(updatedFloor);
    }

    @Transactional
    public void deleteFloor(Long id) {
        if (!floorRepository.existsById(id)) {
            throw new NotFoundException("Floor not found with id: " + id);
        }
        if (tableRepository.existsByFloorId(id)) {
            throw new ConflictException("Cannot delete floor with existing tables");
        }
        floorRepository.deleteById(id);
    }

    // --- Table CRUD ---

    @Transactional(readOnly = true)
    public List<TableResponse> getAllTables() {
        return tableRepository.findAll().stream()
                .map(this::mapToTableResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TableResponse getTableById(Long id) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Table not found with id: " + id));
        return mapToTableResponse(table);
    }

    @Transactional
    public TableResponse createTable(TableRequest request) {
        Floor floor = floorRepository.findById(request.getFloorId())
                .orElseThrow(() -> new ValidationException("Floor not found with id: " + request.getFloorId()));

        if (tableRepository.existsByFloorIdAndNumber(request.getFloorId(), request.getNumber())) {
            throw new ValidationException("Table number already exists on this floor");
        }

        Table table = Table.builder()
                .floor(floor)
                .number(request.getNumber())
                .seats(request.getSeats())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        Table savedTable = tableRepository.save(table);
        return mapToTableResponse(savedTable);
    }

    @Transactional
    public TableResponse updateTable(Long id, TableRequest request) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Table not found with id: " + id));

        Floor floor = floorRepository.findById(request.getFloorId())
                .orElseThrow(() -> new ValidationException("Floor not found with id: " + request.getFloorId()));

        if (tableRepository.existsByFloorIdAndNumberAndIdNot(request.getFloorId(), request.getNumber(), id)) {
            throw new ValidationException("Table number already exists on this floor");
        }

        table.setFloor(floor);
        table.setNumber(request.getNumber());
        table.setSeats(request.getSeats());
        if (request.getActive() != null) {
            table.setActive(request.getActive());
        }

        Table updatedTable = tableRepository.save(table);
        return mapToTableResponse(updatedTable);
    }

    @Transactional
    public void deleteTable(Long id) {
        if (!tableRepository.existsById(id)) {
            throw new NotFoundException("Table not found with id: " + id);
        }
        if (orderRepository.existsByTableId(id)) {
            throw new ConflictException("Cannot delete table with existing orders");
        }
        tableRepository.deleteById(id);
    }

    // --- Helper Mappings ---

    private FloorResponse mapToFloorResponse(Floor floor) {
        return FloorResponse.builder()
                .id(floor.getId())
                .name(floor.getName())
                .build();
    }

    private TableResponse mapToTableResponse(Table table) {
        String status = "available";
        if (table.getId() != null) {
            boolean hasDraftOrders = !orderRepository.findByTableIdAndStatus(table.getId(), OrderStatus.DRAFT).isEmpty();
            status = hasDraftOrders ? "occupied" : "available";
        }

        return TableResponse.builder()
                .id(table.getId())
                .floorId(table.getFloor().getId())
                .floorName(table.getFloor().getName())
                .number(table.getNumber())
                .seats(table.getSeats())
                .active(table.getActive())
                .status(status)
                .build();
    }
}
