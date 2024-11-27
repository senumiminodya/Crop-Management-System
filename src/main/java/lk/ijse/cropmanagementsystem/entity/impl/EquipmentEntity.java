package lk.ijse.cropmanagementsystem.entity.impl;

import jakarta.persistence.*;
import lk.ijse.cropmanagementsystem.entity.StatusOfEquipment;
import lk.ijse.cropmanagementsystem.entity.EquipmentType;
import lk.ijse.cropmanagementsystem.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "staffId", nullable = true)
    private StaffEntity assignedStaff;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fieldCode", nullable = true)
    private FieldEntity assignedField;

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
        this.type = type;
    }

    public StatusOfEquipment getStatus() {
        return status;
    }

    public void setStatus(StatusOfEquipment status) {
        this.status = status;
    }

    public StaffEntity getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(StaffEntity assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public FieldEntity getAssignedField() {
        return assignedField;
    }

    public void setAssignedField(FieldEntity assignedField) {
        this.assignedField = assignedField;
    }
}
