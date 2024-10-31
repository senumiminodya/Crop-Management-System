package lk.ijse.cropmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.VehicleStatus;
import lk.ijse.cropmanagementsystem.dto.impl.VehicleDTO;
import lk.ijse.cropmanagementsystem.entity.impl.VehicleEntity;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.VehicleNotFoundException;
import lk.ijse.cropmanagementsystem.repository.VehicleRepo;
import lk.ijse.cropmanagementsystem.service.VehicleService;
import lk.ijse.cropmanagementsystem.util.AppUtil;
import lk.ijse.cropmanagementsystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private Mapping vehicleMapping;
    @Override
    public void saveVehicle(VehicleDTO vehicleDTO) {
        vehicleDTO.setVehicleCode(AppUtil.generateVehicleCode());
        VehicleEntity savedVehicle =
                vehicleRepo.save(vehicleMapping.toVehicleEntity(vehicleDTO));
        if(savedVehicle == null){
            throw new DataPersistException("Vehicle not saved");
        }
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        return vehicleMapping.asVehicleDtoList( vehicleRepo.findAll());
    }

    @Override
    public VehicleStatus getVehicle(String vehicleCode) {
        if(vehicleRepo.existsById(vehicleCode)){
            var selectedVehicle = vehicleRepo.getReferenceById(vehicleCode);
            return vehicleMapping.toVehicleDto(selectedVehicle);
        }else {
            return new SelectedClassesErrorStatus(2,"Selected note not found");
        }
    }

    @Override
    public void deleteVehicle(String vehicleCode) {
        Optional<VehicleEntity> foundVehicle = vehicleRepo.findById(vehicleCode);
        if (!foundVehicle.isPresent()) {
            throw new VehicleNotFoundException("Vehicle not found");
        }else {
            vehicleRepo.deleteById(vehicleCode);
        }
    }

    @Override
    public void updateVehicle(String vehicleCode, VehicleDTO vehicleDTO) {
        Optional<VehicleEntity> findVehicle = vehicleRepo.findById(vehicleCode);
        if (!findVehicle.isPresent()) {
            throw new VehicleNotFoundException("Vehicle not found");
        }else {
            findVehicle.get().setLicensePlateNumber(vehicleDTO.getLicensePlateNumber());
            findVehicle.get().setCategory(vehicleDTO.getVehicleCategory());
            findVehicle.get().setFuelType(vehicleDTO.getFuelType());
            findVehicle.get().setStatus(vehicleDTO.getStatus());
            findVehicle.get().setRemarks(vehicleDTO.getRemarks());
        }
    }
}
