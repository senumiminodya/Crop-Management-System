package lk.ijse.cropmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.EquipmentStatus;
import lk.ijse.cropmanagementsystem.dto.impl.EquipmentDTO;
import lk.ijse.cropmanagementsystem.entity.EquipmentType;
import lk.ijse.cropmanagementsystem.entity.StatusOfEquipment;
import lk.ijse.cropmanagementsystem.entity.impl.EquipmentEntity;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.EquipmentNotFoundException;
import lk.ijse.cropmanagementsystem.repository.EquipmentRepo;
import lk.ijse.cropmanagementsystem.service.EquipmentService;
import lk.ijse.cropmanagementsystem.util.AppUtil;
import lk.ijse.cropmanagementsystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentRepo equipmentRepo;
    @Autowired
    private Mapping equipmentMapping;
    @Override
    public void saveEquipment(EquipmentDTO equipmentDTO) {
        equipmentDTO.setEquipmentId(AppUtil.generateEquipmentId());
        EquipmentEntity savedEquipment =
                equipmentRepo.save(equipmentMapping.toEquipmentEntity(equipmentDTO));
        if(savedEquipment == null){
            throw new DataPersistException("Equipment not saved");
        }
    }

    @Override
    public List<EquipmentDTO> getAllEquipments() {
        return equipmentMapping.asEquipmentDtoList( equipmentRepo.findAll());
    }

    @Override
    public EquipmentStatus getEquipment(String equipmentId) {
        if(equipmentRepo.existsById(equipmentId)){
            var selectedEquipment = equipmentRepo.getReferenceById(equipmentId);
            return equipmentMapping.toEquipmentDto(selectedEquipment);
        }else {
            return new SelectedClassesErrorStatus(2,"Selected equipment not found");
        }
    }

    @Override
    public void deleteEquipment(String equipmentId) {
        Optional<EquipmentEntity> foundEquipment = equipmentRepo.findById(equipmentId);
        if (!foundEquipment.isPresent()) {
            throw new EquipmentNotFoundException("Equipment not found");
        }else {
            equipmentRepo.deleteById(equipmentId);
        }
    }

    @Override
    public void updateEquipment(String equipmentId, EquipmentDTO equipmentDTO) {
        Optional<EquipmentEntity> findEquipment = equipmentRepo.findById(equipmentId);
        if (!findEquipment.isPresent()) {
            throw new EquipmentNotFoundException("Equipment not found");
        }else {
            findEquipment.get().setName(equipmentDTO.getName());
            findEquipment.get().setType(equipmentDTO.getType());
            findEquipment.get().setStatus(equipmentDTO.getStatus());
        }
    }
}
