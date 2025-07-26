package com.example.pagila_api.controller;

import com.example.pagila_api.model.Staff;
import com.example.pagila_api.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public Page<Staff> getAllStaff(Pageable pageable) {
        return staffService.getAllStaff(pageable);
    }

    @GetMapping("/all")
    public List<Staff> getAllStaffAsList() {
        return staffService.getAllStaff();
    }

    @GetMapping("/{id}")
    public Staff getStaffById(@PathVariable Integer id) {
        return staffService.getStaffById(id);
    }

    @GetMapping("/{id}/with-address")
    public Staff getStaffByIdWithAddress(@PathVariable Integer id) {
        return staffService.getStaffByIdWithAddress(id);
    }

    @GetMapping("/{id}/with-store")
    public Staff getStaffByIdWithStore(@PathVariable Integer id) {
        return staffService.getStaffByIdWithStore(id);
    }

    @GetMapping("/{id}/with-details")
    public Staff getStaffByIdWithDetails(@PathVariable Integer id) {
        return staffService.getStaffByIdWithDetails(id);
    }

    @GetMapping("/by-username/{username}")
    public Staff getStaffByUsername(@PathVariable String username) {
        return staffService.getStaffByUsername(username);
    }

    @GetMapping("/by-email/{email}")
    public Staff getStaffByEmail(@PathVariable String email) {
        return staffService.getStaffByEmail(email);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Staff> authenticateStaff(@RequestParam String username, @RequestParam String password) {
        Staff staff = staffService.authenticateStaff(username, password);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/active")
    public List<Staff> getActiveStaff() {
        return staffService.getActiveStaff();
    }

    @GetMapping("/active/paginated")
    public Page<Staff> getActiveStaffPaginated(Pageable pageable) {
        return staffService.getActiveStaff(pageable);
    }

    @GetMapping("/inactive")
    public List<Staff> getInactiveStaff() {
        return staffService.getInactiveStaff();
    }

    @GetMapping("/by-store/{storeId}")
    public List<Staff> getStaffByStoreId(@PathVariable Integer storeId) {
        return staffService.getStaffByStoreId(storeId);
    }

    @GetMapping("/by-store/{storeId}/paginated")
    public Page<Staff> getStaffByStoreIdPaginated(@PathVariable Integer storeId, Pageable pageable) {
        return staffService.getStaffByStoreId(storeId, pageable);
    }

    @GetMapping("/by-store/{storeId}/active")
    public List<Staff> getActiveStaffByStoreId(@PathVariable Integer storeId) {
        return staffService.getActiveStaffByStoreId(storeId);
    }

    @PostMapping
    public ResponseEntity<Staff> createStaff(@Valid @RequestBody Staff staff) {
        Staff createdStaff = staffService.createStaff(staff);
        return ResponseEntity.status(201).body(createdStaff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Staff> updateStaff(@PathVariable Integer id, @Valid @RequestBody Staff staff) {
        Staff updatedStaff = staffService.updateStaff(id, staff);
        return ResponseEntity.ok(updatedStaff);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Staff> deactivateStaff(@PathVariable Integer id) {
        Staff staff = staffService.deactivateStaff(id);
        return ResponseEntity.ok(staff);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Staff> activateStaff(@PathVariable Integer id) {
        Staff staff = staffService.activateStaff(id);
        return ResponseEntity.ok(staff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Integer id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    // Search endpoints
    @GetMapping("/search")
    public List<Staff> searchStaffByName(@RequestParam String name) {
        return staffService.searchStaffByName(name);
    }

    @GetMapping("/search/paginated")
    public Page<Staff> searchStaffByNamePaginated(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Pageable pageable) {
        return staffService.searchStaffByName(firstName != null ? firstName : "",
                lastName != null ? lastName : "", pageable);
    }

    // Manager endpoints
    @GetMapping("/managers")
    public List<Staff> getManagers() {
        return staffService.getManagers();
    }

    @GetMapping("/non-managers")
    public List<Staff> getNonManagers() {
        return staffService.getNonManagers();
    }

    // Performance metrics
    @GetMapping("/{id}/rental-count")
    public ResponseEntity<Long> getRentalCountByStaffId(@PathVariable Integer id) {
        Long count = staffService.getRentalCountByStaffId(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/payment-count")
    public ResponseEntity<Long> getPaymentCountByStaffId(@PathVariable Integer id) {
        Long count = staffService.getPaymentCountByStaffId(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/rental-count/by-date-range")
    public ResponseEntity<Long> getRentalCountByStaffIdAndDateRange(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long count = staffService.getRentalCountByStaffIdAndDateRange(id, startDate, endDate);
        return ResponseEntity.ok(count);
    }

    // Analytics endpoints
    @GetMapping("/analytics/by-rental-count")
    public List<Object[]> getStaffByRentalCount() {
        return staffService.getStaffByRentalCount();
    }

    @GetMapping("/analytics/by-payment-count")
    public List<Object[]> getStaffByPaymentCount() {
        return staffService.getStaffByPaymentCount();
    }

    // Password management
    @PutMapping("/{id}/password")
    public ResponseEntity<Staff> updatePassword(@PathVariable Integer id, @RequestParam String newPassword) {
        Staff staff = staffService.updatePassword(id, newPassword);
        return ResponseEntity.ok(staff);
    }

    // Picture management
    @PostMapping(value = "/{id}/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Staff> uploadPicture(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            Staff staff = staffService.updatePicture(id, file.getBytes());
            return ResponseEntity.ok(staff);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload picture", e);
        }
    }

    @GetMapping(value = "/{id}/picture", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPicture(@PathVariable Integer id) {
        Staff staff = staffService.getStaffById(id);
        if (staff.getPicture() != null) {
            return ResponseEntity.ok(staff.getPicture());
        }
        return ResponseEntity.notFound().build();
    }
}