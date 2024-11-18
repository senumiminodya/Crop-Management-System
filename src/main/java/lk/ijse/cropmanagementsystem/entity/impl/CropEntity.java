package lk.ijse.cropmanagementsystem.entity.impl;

import jakarta.persistence.*;
import lk.ijse.cropmanagementsystem.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @ManyToOne
    @JoinColumn(name = "fieldCode")
    private FieldEntity field;
}
