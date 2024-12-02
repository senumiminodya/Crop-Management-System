package lk.ijse.cropmanagementsystem.util;

import lk.ijse.cropmanagementsystem.dto.impl.*;
import lk.ijse.cropmanagementsystem.entity.impl.*;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.repository.CropRepo;
import lk.ijse.cropmanagementsystem.repository.FieldRepo;
import lk.ijse.cropmanagementsystem.repository.StaffRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapping {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private FieldRepo fieldRepo;
    @Autowired
    private CropRepo cropRepo;
    // for user mapping
    public UserEntity toUserEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }
    public UserDTO toUserDTO(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDTO.class);
    }
    public List<UserDTO> asUserDTOList(List<UserEntity> userEntities) {
        return modelMapper.map(userEntities, new TypeToken<List<UserDTO>>(){}.getType());
    }
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

        //return modelMapper.map(fieldEntity, FieldDTO.class);
        FieldDTO fieldDTO = modelMapper.map(fieldEntity, FieldDTO.class);
        // Replace staff entities with staff IDs
        fieldDTO.setStaff(
                fieldEntity.getStaff().stream()
                        .map(StaffEntity::getId) // Extract IDs from StaffEntity
                        .collect(Collectors.toList())
        );
        return fieldDTO;
    }
    public List<FieldDTO> asFieldDtoList(List<FieldEntity> fieldEntities) {
        return modelMapper.map(fieldEntities, new TypeToken<List<FieldDTO>>() {}.getType());
    }
    //for monitoringLog mapping
    public MonitoringLogEntity toMonitoringLogEntity(MonitoringLogDTO monitoringLogDTO) {
        //return modelMapper.map(monitoringLogDTO, MonitoringLogEntity.class);
        MonitoringLogEntity entity = new MonitoringLogEntity();
        entity.setLogCode(monitoringLogDTO.getLogCode());
        entity.setLogDate(monitoringLogDTO.getLogDate());
        entity.setObservation(monitoringLogDTO.getObservation());
        entity.setObservedImage(monitoringLogDTO.getObservedImage());

        List<FieldEntity> fieldEntities = monitoringLogDTO.getFields().stream()
                .map(field -> fieldRepo.findById(field)
                        .orElseThrow(() -> new DataPersistException("Field not found")))
                .collect(Collectors.toList());
        entity.setFields(fieldEntities);

        List<CropEntity> cropEntities = monitoringLogDTO.getCrops().stream()
                .map(crop -> cropRepo.findById(crop)
                        .orElseThrow(() -> new DataPersistException("Crop not found")))
                .collect(Collectors.toList());
        entity.setCrops(cropEntities);

        List<StaffEntity> staffEntities = monitoringLogDTO.getStaff().stream()
                .map(staff -> staffRepo.findById(staff)
                        .orElseThrow(() -> new DataPersistException("Staff not found")))
                .collect(Collectors.toList());
        entity.setStaff(staffEntities);

        return entity;
    }
    public MonitoringLogDTO toMonitoringLogDto(MonitoringLogEntity monitoringLogEntity) {
        //return modelMapper.map(monitoringLogEntity, MonitoringLogDTO.class);
        MonitoringLogDTO dto = new MonitoringLogDTO();
        dto.setLogCode(monitoringLogEntity.getLogCode());
        dto.setLogDate(monitoringLogEntity.getLogDate());
        dto.setObservation(monitoringLogEntity.getObservation());
        dto.setObservedImage(monitoringLogEntity.getObservedImage());
        dto.setFields(monitoringLogEntity.getFields().stream()
                .map(FieldEntity::getFieldCode)
                .toList());
        dto.setCrops(monitoringLogEntity.getCrops().stream()
                .map(CropEntity::getCropCode)
                .toList());
        dto.setStaff(monitoringLogEntity.getStaff().stream()
                .map(StaffEntity::getId)
                .toList());
        return dto;
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
