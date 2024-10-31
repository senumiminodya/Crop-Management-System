package lk.ijse.cropmanagementsystem.service;

import lk.ijse.cropmanagementsystem.dto.StaffStatus;
import lk.ijse.cropmanagementsystem.dto.impl.StaffDTO;

import java.util.List;

public interface StaffService {
    void saveStaff(StaffDTO staffDTO);
    List<StaffDTO> getAllStaff();
    StaffStatus getStaff(String staffId);
    void deleteStaff(String staffId);
    void updateStaff(String staffId, StaffDTO staffDTO);
}
