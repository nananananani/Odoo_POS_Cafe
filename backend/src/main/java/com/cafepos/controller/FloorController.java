package com.cafepos.controller;

import com.cafepos.dto.request.FloorRequest;
import com.cafepos.dto.response.FloorResponse;
import com.cafepos.service.FloorTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/floors")
@RequiredArgsConstructor
public class FloorController {

    private final FloorTableService floorTableService;

    @GetMapping
    public ResponseEntity<List<FloorResponse>> getAllFloors() {
        return ResponseEntity.ok(floorTableService.getAllFloors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FloorResponse> getFloorById(@PathVariable Long id) {
        return ResponseEntity.ok(floorTableService.getFloorById(id));
    }

    @PostMapping
    public ResponseEntity<FloorResponse> createFloor(@Valid @RequestBody FloorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(floorTableService.createFloor(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FloorResponse> updateFloor(@PathVariable Long id, @Valid @RequestBody FloorRequest request) {
        return ResponseEntity.ok(floorTableService.updateFloor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFloor(@PathVariable Long id) {
        floorTableService.deleteFloor(id);
        return ResponseEntity.noContent().build();
    }
}
