package lk.ijse.cropmanagementsystem.service;

import lk.ijse.cropmanagementsystem.dto.VehicleStatus;
import lk.ijse.cropmanagementsystem.dto.impl.VehicleDTO;

import java.util.List;

public interface VehicleService {
    void saveVehicle(VehicleDTO vehicleDTO);
    List<VehicleDTO> getAllVehicles();
    VehicleStatus getVehicle(String vehicleCode);
    void deleteVehicle(String vehicleCode);
    void updateVehicle(String vehicleCode, VehicleDTO vehicleDTO);
}
