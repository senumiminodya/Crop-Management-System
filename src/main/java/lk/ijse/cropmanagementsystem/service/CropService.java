package lk.ijse.cropmanagementsystem.service;

import lk.ijse.cropmanagementsystem.dto.CropStatus;
import lk.ijse.cropmanagementsystem.dto.impl.CropDTO;

import java.util.List;

public interface CropService {
    void saveCrop(CropDTO cropDTO);
    List<CropDTO> getAllCrops();
    CropStatus getCrop(String cropCode);
    void deleteCrop(String cropCode);
    void updateCrop(String cropCode, CropDTO cropDTO);
}
