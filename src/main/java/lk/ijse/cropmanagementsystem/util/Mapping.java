package lk.ijse.cropmanagementsystem.util;

import lk.ijse.cropmanagementsystem.dto.impl.*;
import lk.ijse.cropmanagementsystem.entity.impl.*;
import lk.ijse.cropmanagementsystem.repository.StaffRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapping {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StaffRepo staffRepo;
    //for crop mapping
    public CropEntity toCropEntity(CropDTO cropDTO) {
        return modelMapper.map(cropDTO, CropEntity.class);
    }
    public CropDTO toCropDto(CropEntity cropEntity) {
        return modelMapper.map(cropEntity, CropDTO.class);
    }
    public List<CropDTO> asCropDtoList(List<CropEntity> cropEntities) {
        return modelMapper.map(cropEntities, new TypeToken<List<CropDTO>>() {}.getType());
    }
    //for equipment mapping
    public EquipmentEntity toEquipmentEntity(EquipmentDTO equipmentDTO, StaffEntity staff, FieldEntity field) {
        EquipmentEntity equipmentEntity = modelMapper.map(equipmentDTO, EquipmentEntity.class);
        equipmentEntity.setAssignedStaff(staff);
        equipmentEntity.setAssignedField(field);
        return equipmentEntity;
    }
    public EquipmentDTO toEquipmentDto(EquipmentEntity equipmentEntity) {
        return modelMapper.map(equipmentEntity, EquipmentDTO.class);
    }
    public List<EquipmentDTO> asEquipmentDtoList(List<EquipmentEntity> equipmentEntities) {
        return modelMapper.map(equipmentEntities, new TypeToken<List<EquipmentDTO>>() {}.getType());
    }
    //for field mapping
    public FieldEntity toFieldEntity(FieldDTO fieldDTO) {

        return modelMapper.map(fieldDTO, FieldEntity.class);
    }
    public FieldDTO toFieldDto(FieldEntity fieldEntity) {
        return modelMapper.map(fieldEntity, FieldDTO.class);
    }
    public List<FieldDTO> asFieldDtoList(List<FieldEntity> fieldEntities) {
        return modelMapper.map(fieldEntities, new TypeToken<List<FieldDTO>>() {}.getType());
    }
    //for monitoringLog mapping
    public MonitoringLogEntity toMonitoringLogEntity(MonitoringLogDTO monitoringLogDTO) {
        return modelMapper.map(monitoringLogDTO, MonitoringLogEntity.class);
    }
    public MonitoringLogDTO toMonitoringLogDto(MonitoringLogEntity monitoringLogEntity) {
        return modelMapper.map(monitoringLogEntity, MonitoringLogDTO.class);
    }
    public List<MonitoringLogDTO> asMonitoringLogDtoList(List<MonitoringLogEntity> monitoringLogEntities) {
        return modelMapper.map(monitoringLogEntities, new TypeToken<List<MonitoringLogDTO>>() {}.getType());
    }
    //for staff mapping
    public StaffEntity toStaffEntity(StaffDTO staffDTO) {
        return modelMapper.map(staffDTO, StaffEntity.class);
    }
    public StaffDTO toStaffDto(StaffEntity staffEntity) {
        return modelMapper.map(staffEntity, StaffDTO.class);
    }
    public List<StaffDTO> asStaffDtoList(List<StaffEntity> staffEntities) {
        return modelMapper.map(staffEntities, new TypeToken<List<StaffDTO>>() {}.getType());
    }
    //for vehicle mapping
    public VehicleEntity toVehicleEntity(VehicleDTO vehicleDTO) {
        VehicleEntity vehicleEntity = modelMapper.map(vehicleDTO, VehicleEntity.class);
        if (vehicleDTO.getStaffId() != null) {
            StaffEntity staff = staffRepo.findById(vehicleDTO.getStaffId()).orElse(null);
            if (staff != null) {
                vehicleEntity.setStaff(staff);
            } else {
                // Log or handle the scenario where staff is not found in the database
                System.out.println("Staff with ID " + vehicleDTO.getStaffId() + " not found.");
            }
            vehicleEntity.setStaff(staff);
        }
        return vehicleEntity;
    }
    public VehicleDTO toVehicleDto(VehicleEntity vehicleEntity) {
        return modelMapper.map(vehicleEntity, VehicleDTO.class);
    }
    public List<VehicleDTO> asVehicleDtoList(List<VehicleEntity> vehicleEntities) {
        return modelMapper.map(vehicleEntities, new TypeToken<List<VehicleDTO>>() {}.getType());
    }
}
