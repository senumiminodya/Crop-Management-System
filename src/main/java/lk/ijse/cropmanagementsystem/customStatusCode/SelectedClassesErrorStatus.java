package lk.ijse.cropmanagementsystem.customStatusCode;

import lk.ijse.cropmanagementsystem.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectedClassesErrorStatus implements CropStatus, EquipmentStatus, FieldStatus, MonitoringLogStatus, StaffStatus, UserStatus, VehicleStatus {
    private int status;
    private String statusMessage;
}
