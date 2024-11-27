package lk.ijse.cropmanagementsystem.customStatusCode;

import lk.ijse.cropmanagementsystem.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectedClassesErrorStatus implements CropStatus, EquipmentStatus, FieldStatus, MonitoringLogStatus, StaffStatus, VehicleStatus, UserStatus, Serializable, SuperDTO {
    private int status;
    private String statusMessage;
}
