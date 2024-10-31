package lk.ijse.cropmanagementsystem.dto.impl;

import lk.ijse.cropmanagementsystem.dto.EquipmentStatus;
import lk.ijse.cropmanagementsystem.entity.EquipmentType;
import lk.ijse.cropmanagementsystem.entity.StatusOfEquipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EquipmentDTO implements EquipmentStatus {
    private String equipmentId;
    private String name;
    private EquipmentType type;
    private StatusOfEquipment status;
    private String staffId;
    private String fieldCode;
}
