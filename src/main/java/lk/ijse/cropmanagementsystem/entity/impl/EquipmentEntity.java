package lk.ijse.cropmanagementsystem.entity.impl;

import jakarta.persistence.*;
import lk.ijse.cropmanagementsystem.entity.StatusOfEquipment;
import lk.ijse.cropmanagementsystem.entity.EquipmentType;
import lk.ijse.cropmanagementsystem.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "equipment")
public class EquipmentEntity implements SuperEntity {
    @Id
    private String equipmentId;
    private String name;

    @Enumerated(EnumType.STRING)
    private EquipmentType type;

    @Enumerated(EnumType.STRING)
    private StatusOfEquipment status;

    @ManyToOne
    @JoinColumn(name = "staffId")
    private StaffEntity assignedStaff;

    @ManyToOne
    @JoinColumn(name = "fieldCode")
    private FieldEntity assignedField;
}
