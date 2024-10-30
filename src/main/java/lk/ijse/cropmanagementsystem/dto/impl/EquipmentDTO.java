package lk.ijse.cropmanagementsystem.dto.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EquipmentDTO {
    private String equipmentId;
    private String name;
    private String type;
    private String status;
    private StaffDTO assignedStaff;
    private FieldDTO assignedField;
}
