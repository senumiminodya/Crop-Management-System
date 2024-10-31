package lk.ijse.cropmanagementsystem.service;

import lk.ijse.cropmanagementsystem.dto.EquipmentStatus;
import lk.ijse.cropmanagementsystem.dto.impl.EquipmentDTO;

import java.util.List;

public interface EquipmentService {
    void saveEquipment(EquipmentDTO equipmentDTO);
    List<EquipmentDTO> getAllEquipments();
    EquipmentStatus getEquipment(String equipmentId);
    void deleteEquipment(String equipmentId);
    void updateEquipment(String equipmentId, EquipmentDTO equipmentDTO);
}
