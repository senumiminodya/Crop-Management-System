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

        /*FieldEntity savedField =
                fieldRepo.save(fieldMapping.toFieldEntity(fieldDTO));
        if(savedField == null){
            throw new DataPersistException("Field not saved");
        }*/
        // Load existing staff entities (by ID) and set them to fieldEntity
        FieldEntity fieldEntity = fieldMapping.toFieldEntity(fieldDTO);
        List<StaffEntity> staffEntities = fieldDTO.getStaff().stream()
                .map(staffId -> staffRepo.getReferenceById(staffId)) // uses a proxy to avoid unsaved entities
                .collect(Collectors.toList());
        fieldEntity.setStaff(staffEntities);

        // Save fieldEntity which now has managed StaffEntity references
        fieldRepo.save(fieldEntity);
    }

    @Override
    public List<FieldDTO> getAllFields() {
        return fieldMapping.asFieldDtoList( fieldRepo.findAll());
    }

    @Override
    public FieldStatus getField(String fieldCode) {
        if(fieldRepo.existsById(fieldCode)){
            var selectedField = fieldRepo.getReferenceById(fieldCode);
            return fieldMapping.toFieldDto(selectedField);
        }else {
            return new SelectedClassesErrorStatus(2,"Selected field not found");
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
