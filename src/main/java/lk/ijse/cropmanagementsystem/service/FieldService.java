package lk.ijse.cropmanagementsystem.service;

import lk.ijse.cropmanagementsystem.dto.FieldStatus;
import lk.ijse.cropmanagementsystem.dto.impl.FieldDTO;


import java.util.List;

public interface FieldService {
    void saveField(FieldDTO fieldDTO);
    List<FieldDTO> getAllFields();
    FieldStatus getField(String fieldCode);
    void deleteField(String fieldCode);
    void updateField(String fieldCode, FieldDTO fieldDTO);
}
