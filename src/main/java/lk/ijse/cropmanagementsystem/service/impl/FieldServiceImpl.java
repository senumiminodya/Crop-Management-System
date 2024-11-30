package lk.ijse.cropmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.FieldStatus;
import lk.ijse.cropmanagementsystem.dto.impl.FieldDTO;
import lk.ijse.cropmanagementsystem.entity.impl.FieldEntity;
import lk.ijse.cropmanagementsystem.entity.impl.StaffEntity;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.FieldNotFoundException;
import lk.ijse.cropmanagementsystem.repository.FieldRepo;
import lk.ijse.cropmanagementsystem.repository.StaffRepo;
import lk.ijse.cropmanagementsystem.service.FieldService;
import lk.ijse.cropmanagementsystem.util.AppUtil;
import lk.ijse.cropmanagementsystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FieldServiceImpl implements FieldService {
    @Autowired
    private FieldRepo fieldRepo;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private Mapping fieldMapping;
    @Override
    public void saveField(FieldDTO fieldDTO) {
        fieldDTO.setFieldCode(AppUtil.generateFieldCode());

        // Load existing staff entities (by ID) and set them to fieldEntity
        /*FieldEntity fieldEntity = fieldMapping.toFieldEntity(fieldDTO);
        List<StaffEntity> staffEntities = fieldDTO.getStaff().stream()
                .map(staffId -> staffRepo.getReferenceById(staffId)) // uses a proxy to avoid unsaved entities
                .collect(Collectors.toList());
        fieldEntity.setStaff(staffEntities);*/

        // Save fieldEntity which now has managed StaffEntity references
        /*fieldRepo.save(fieldEntity);*/
        // Map DTO to Entity using modelMapper
        FieldEntity fieldEntity = fieldMapping.toFieldEntity(fieldDTO);

        // Map staff IDs to StaffEntity references (use JPA proxies)
        List<StaffEntity> staffEntities = fieldDTO.getStaff().stream()
                .map(staffId -> staffRepo.getReferenceById(staffId)) // Get proxy references
                .collect(Collectors.toList());

        // Set the staff entities to the field entity
        fieldEntity.setStaff(staffEntities);

        // Save the field entity
        fieldRepo.save(fieldEntity);
    }

    @Override
    public List<FieldDTO> getAllFields() {

        //return fieldMapping.asFieldDtoList( fieldRepo.findAll());
        // Fetch all fields and map them to FieldDTO
        return fieldRepo.findAll().stream()
                .map(fieldEntity -> {
                    FieldDTO fieldDTO = fieldMapping.toFieldDto(fieldEntity);
                    // Replace staff entities with staff IDs
                    fieldDTO.setStaff(
                            fieldEntity.getStaff().stream()
                                    .map(StaffEntity::getId) // Extract IDs from StaffEntity
                                    .collect(Collectors.toList())
                    );
                    return fieldDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public FieldStatus getField(String fieldCode) {
        /*if(fieldRepo.existsById(fieldCode)){
            var selectedField = fieldRepo.getReferenceById(fieldCode);
            return fieldMapping.toFieldDto(selectedField);
        }else {
            return new SelectedClassesErrorStatus(2,"Selected field not found");
        }*/
        if (fieldRepo.existsById(fieldCode)) {
            var selectedField = fieldRepo.getReferenceById(fieldCode);
            FieldDTO fieldDTO = fieldMapping.toFieldDto(selectedField);
            // Replace staff entities with staff IDs
            fieldDTO.setStaff(
                    selectedField.getStaff().stream()
                            .map(StaffEntity::getId) // Extract IDs from StaffEntity
                            .collect(Collectors.toList())
            );
            return fieldDTO;
        } else {
            return new SelectedClassesErrorStatus(2, "Selected field not found");
        }
    }

    @Override
    public void deleteField(String fieldCode) {
        Optional<FieldEntity> foundField = fieldRepo.findById(fieldCode);
        if (!foundField.isPresent()) {
            throw new FieldNotFoundException("Field not found");
        }else {
            fieldRepo.deleteById(fieldCode);
        }
    }

    @Override
    public void updateField(String fieldCode, FieldDTO fieldDTO) {
        Optional<FieldEntity> findField = fieldRepo.findById(fieldCode);
        if (!findField.isPresent()) {
            throw new FieldNotFoundException("Field not found");
        }else {
            findField.get().setFieldName(fieldDTO.getFieldName());
            findField.get().setLocation(fieldDTO.getFieldLocation());
            findField.get().setExtentSize(fieldDTO.getExtentSize());
            findField.get().setFieldImage1(fieldDTO.getFieldImage1());
            findField.get().setFieldImage2(fieldDTO.getFieldImage2());
            List<StaffEntity> staffEntities = fieldDTO.getStaff().stream()
                    .map(staffId -> staffRepo.getReferenceById(staffId)) // uses a proxy to avoid unsaved entities
                    .collect(Collectors.toList());
            findField.get().setStaff(staffEntities);
        }
    }
}
