package com.example.pagila_api.service;

import com.example.pagila_api.exception.ResourceNotFoundException;
import com.example.pagila_api.model.Staff;
import com.example.pagila_api.repository.StaffRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Page<Staff> getAllStaff(Pageable pageable) {
        return staffRepository.findAll(pageable);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff getStaffById(Integer id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + id));
    }

    public Staff getStaffByIdWithAddress(Integer id) {
        return staffRepository.findByIdWithAddress(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + id));
    }

    public Staff getStaffByIdWithStore(Integer id) {
        return staffRepository.findByIdWithStore(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + id));
    }

    public Staff getStaffByIdWithDetails(Integer id) {
        return staffRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with ID: " + id));
    }

    // Authentication methods
    public Staff getStaffByUsername(String username) {
        return staffRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with username: " + username));
    }

    public Staff authenticateStaff(String username, String password) {
        return staffRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));
    }

    public Staff getStaffByEmail(String email) {
        return staffRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + email));
    }

    // Active staff queries
    public List<Staff> getActiveStaff() {
        return staffRepository.findByActiveTrue();
    }

    public Page<Staff> getActiveStaff(Pageable pageable) {
        return staffRepository.findByActiveTrue(pageable);
    }

    public List<Staff> getInactiveStaff() {
        return staffRepository.findByActiveFalse();
    }

    // Store-based queries
    public List<Staff> getStaffByStoreId(Integer storeId) {
        return staffRepository.findByStoreId(storeId);
    }

    public Page<Staff> getStaffByStoreId(Integer storeId, Pageable pageable) {
        return staffRepository.findByStoreId(storeId, pageable);
    }

    public List<Staff> getActiveStaffByStoreId(Integer storeId) {
        return staffRepository.findByStoreIdAndActiveTrue(storeId);
    }

    @Transactional
    public Staff createStaff(Staff staff) {
        // Validate unique constraints
        if (staffRepository.existsByUsername(staff.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + staff.getUsername());
        }

        if (staff.getEmail() != null && staffRepository.existsByEmail(staff.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + staff.getEmail());
        }

        // Set default values
        if (staff.getActive() == null) {
            staff.setActive(true);
        }

        return staffRepository.save(staff);
    }

    @Transactional
    public Staff updateStaff(Integer id, Staff updatedStaff) {
        Staff existingStaff = getStaffById(id);

        // Check unique constraints (excluding current staff)
        if (!existingStaff.getUsername().equals(updatedStaff.getUsername()) &&
                staffRepository.existsByUsernameAndStaffIdNot(updatedStaff.getUsername(), id)) {
            throw new IllegalArgumentException("Username already exists: " + updatedStaff.getUsername());
        }

        if (updatedStaff.getEmail() != null &&
                !updatedStaff.getEmail().equals(existingStaff.getEmail()) &&
                staffRepository.existsByEmailAndStaffIdNot(updatedStaff.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists: " + updatedStaff.getEmail());
        }

        // Update fields
        existingStaff.setFirstName(updatedStaff.getFirstName());
        existingStaff.setLastName(updatedStaff.getLastName());
        existingStaff.setAddressId(updatedStaff.getAddressId());
        existingStaff.setEmail(updatedStaff.getEmail());
        existingStaff.setStoreId(updatedStaff.getStoreId());
        existingStaff.setActive(updatedStaff.getActive());
        existingStaff.setUsername(updatedStaff.getUsername());

        // Only update password if provided
        if (updatedStaff.getPassword() != null && !updatedStaff.getPassword().isEmpty()) {
            existingStaff.setPassword(updatedStaff.getPassword());
        }

        // Update picture if provided
        if (updatedStaff.getPicture() != null) {
            existingStaff.setPicture(updatedStaff.getPicture());
        }

        return staffRepository.save(existingStaff);
    }

    @Transactional
    public Staff deactivateStaff(Integer id) {
        Staff staff = getStaffById(id);

        // Check if staff is managing any stores
        if (staff.isManager()) {
            throw new IllegalStateException("Cannot deactivate staff who is managing stores");
        }

        staff.setActive(false);
        return staffRepository.save(staff);
    }

    @Transactional
    public Staff activateStaff(Integer id) {
        Staff staff = getStaffById(id);
        staff.setActive(true);
        return staffRepository.save(staff);
    }

    @Transactional
    public void deleteStaff(Integer id) {
        Staff staff = getStaffById(id);

        // Check if staff has associated rentals or payments
        Long rentalCount = staffRepository.countRentalsByStaffId(id);
        Long paymentCount = staffRepository.countPaymentsByStaffId(id);

        if (rentalCount > 0 || paymentCount > 0) {
            throw new IllegalStateException("Cannot delete staff with associated rentals or payments");
        }

        // Check if staff is managing any stores
        if (staff.isManager()) {
            throw new IllegalStateException("Cannot delete staff who is managing stores");
        }

        staffRepository.deleteById(id);
    }

    // Search methods
    public List<Staff> searchStaffByName(String name) {
        return staffRepository.findByNameContaining(name);
    }

    public Page<Staff> searchStaffByName(String firstName, String lastName, Pageable pageable) {
        return staffRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                firstName, lastName, pageable);
    }

    // Manager queries
    public List<Staff> getManagers() {
        return staffRepository.findManagers();
    }

    public List<Staff> getNonManagers() {
        return staffRepository.findNonManagers();
    }

    // Performance metrics
    public Long getRentalCountByStaffId(Integer staffId) {
        return staffRepository.countRentalsByStaffId(staffId);
    }

    public Long getPaymentCountByStaffId(Integer staffId) {
        return staffRepository.countPaymentsByStaffId(staffId);
    }

    public Long getRentalCountByStaffIdAndDateRange(Integer staffId, LocalDateTime startDate, LocalDateTime endDate) {
        return staffRepository.countRentalsByStaffIdAndDateRange(staffId, startDate, endDate);
    }

    // Analytics
    public List<Object[]> getStaffByRentalCount() {
        return staffRepository.findStaffByRentalCount();
    }

    public List<Object[]> getStaffByPaymentCount() {
        return staffRepository.findStaffByPaymentCount();
    }

    // Password management
    @Transactional
    public Staff updatePassword(Integer staffId, String newPassword) {
        Staff staff = getStaffById(staffId);
        staff.setPassword(newPassword);
        return staffRepository.save(staff);
    }

    // Picture management
    @Transactional
    public Staff updatePicture(Integer staffId, byte[] picture) {
        Staff staff = getStaffById(staffId);
        staff.setPicture(picture);
        return staffRepository.save(staff);
    }
}