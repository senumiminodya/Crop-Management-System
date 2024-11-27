package lk.ijse.cropmanagementsystem.entity.impl;

import jakarta.persistence.*;
import lk.ijse.cropmanagementsystem.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "crop")
public class CropEntity implements SuperEntity {
    @Id
    private String cropCode;
    private String commonName;
    private String scientificName;

    @Column(columnDefinition = "VARCHAR(255)")
    private String cropImage;

    private String category;
    private String season;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fieldCode", nullable = false)
    private FieldEntity field;

    public String getCropCode() {
        return cropCode;
    }

    public void setCropCode(String cropCode) {
        this.cropCode = cropCode;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCropImage() {
        return cropImage;
    }

    public void setCropImage(String cropImage) {
        this.cropImage = cropImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public FieldEntity getField() {
        return field;
    }

    public void setField(FieldEntity field) {
        this.field = field;
    }
}
