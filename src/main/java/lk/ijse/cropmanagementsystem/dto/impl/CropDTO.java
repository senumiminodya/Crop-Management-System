package lk.ijse.cropmanagementsystem.dto.impl;

import lk.ijse.cropmanagementsystem.dto.CropStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CropDTO implements CropStatus {
    private String cropCode;
    private String cropCommonName;
    private String cropScientificName;
    private String cropImage;
    private String category;
    private String cropSeason;
    private String fieldCode;

    public CropDTO(String cropCommonName, String cropScientificName, String cropImage, String category, String cropSeason, String fieldCode) {
        this.cropCommonName = cropCommonName;
        this.cropScientificName = cropScientificName;
        this.cropImage = cropImage;
        this.category = category;
        this.cropSeason = cropSeason;
        this.fieldCode = fieldCode;
    }
}
