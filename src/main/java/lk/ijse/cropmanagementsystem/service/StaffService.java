package lk.ijse.cropmanagementsystem.service;

import lk.ijse.cropmanagementsystem.dto.StaffStatus;
import lk.ijse.cropmanagementsystem.dto.impl.StaffDTO;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    StaffDTO saveStaff(StaffDTO staffDTO);
    List<StaffDTO> getAllStaff();
    StaffStatus getStaff(String staffId);
    void deleteStaff(String staffId);
    void updateStaff(String staffId, StaffDTO staffDTO);
    Optional findByEmail(String email);
}
