package lk.ijse.cropmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.CropStatus;
import lk.ijse.cropmanagementsystem.dto.impl.CropDTO;
import lk.ijse.cropmanagementsystem.entity.impl.CropEntity;
import lk.ijse.cropmanagementsystem.entity.impl.FieldEntity;
import lk.ijse.cropmanagementsystem.exception.CropNotFoundException;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.repository.CropRepo;
import lk.ijse.cropmanagementsystem.repository.FieldRepo;
import lk.ijse.cropmanagementsystem.service.CropService;
import lk.ijse.cropmanagementsystem.util.AppUtil;
import lk.ijse.cropmanagementsystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CropServiceImpl implements CropService {
    @Autowired
    private CropRepo cropRepo;
    @Autowired
    private FieldRepo fieldRepo;
    @Autowired
    private Mapping cropMapping;
    @Override
    public void saveCrop(CropDTO cropDTO) {
        cropDTO.setCropCode(AppUtil.generateCropCode());
        CropEntity savedCrop =
                cropRepo.save(cropMapping.toCropEntity(cropDTO));
        if(savedCrop == null){
            throw new DataPersistException("Crop not saved");
        }
    }

    @Override
    public List<CropDTO> getAllCrops() {
        return cropMapping.asCropDtoList( cropRepo.findAll());
    }

    @Override
    public CropStatus getCrop(String cropCode) {
        if(cropRepo.existsById(cropCode)){
            var selectedCrop = cropRepo.getReferenceById(cropCode);
            return cropMapping.toCropDto(selectedCrop);
        }else {
            return new SelectedClassesErrorStatus(2,"Selected crop not found");
        }
    }

    @Override
    public void deleteCrop(String cropCode) {
        Optional<CropEntity> foundCrop = cropRepo.findById(cropCode);
        if (!foundCrop.isPresent()) {
            throw new CropNotFoundException("Crop not found");
        }else {
            cropRepo.deleteById(cropCode);
        }
    }

    @Override
    public void updateCrop(String cropCode, CropDTO cropDTO) {
        Optional<CropEntity> findCrop = cropRepo.findById(cropCode);
        if (!findCrop.isPresent()) {
            throw new CropNotFoundException("Crop not found");
        }else {
            findCrop.get().setCommonName(cropDTO.getCropCommonName());
            findCrop.get().setScientificName(cropDTO.getCropScientificName());
            findCrop.get().setCropImage(cropDTO.getCropImage());
            findCrop.get().setCategory(cropDTO.getCategory());
            findCrop.get().setSeason(cropDTO.getCropSeason());
            FieldEntity field = fieldRepo.findById(cropDTO.getFieldCode())
                    .orElseThrow(() -> new DataPersistException("Field not found"));
            findCrop.get().setField(field);
        }
    }
}
