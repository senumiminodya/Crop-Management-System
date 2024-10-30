package lk.ijse.cropmanagementsystem.dto.impl;

import lk.ijse.cropmanagementsystem.entity.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO {
    private String vehicleCode;
    private String licensePlateNumber;
    private String vehicleCategory;
    private String fuelType;
    private VehicleStatus status;
    private StaffDTO assignedStaff;
    private String remarks;
}
