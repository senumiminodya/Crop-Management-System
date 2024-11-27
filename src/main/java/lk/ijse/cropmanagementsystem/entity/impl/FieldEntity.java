package lk.ijse.cropmanagementsystem.entity.impl;

import jakarta.persistence.*;
import lk.ijse.cropmanagementsystem.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "field")
public class FieldEntity implements SuperEntity {
    @Id
    private String fieldCode;
    private String fieldName;

    private Point location; // Using a String here for the GPS point, assuming spatial data handling.

    private double extentSize;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CropEntity> crops;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "field_staff",
            joinColumns = @JoinColumn(name = "fieldCode"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<StaffEntity> staff;

    @Column(columnDefinition = "VARCHAR(255)")
    private String fieldImage1;

    @Column(columnDefinition = "VARCHAR(255)")
    private String fieldImage2;

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public double getExtentSize() {
        return extentSize;
    }

    public void setExtentSize(double extentSize) {
        this.extentSize = extentSize;
    }

    public List<CropEntity> getCrops() {
        return crops;
    }

    public void setCrops(List<CropEntity> crops) {
        this.crops = crops;
    }

    public List<StaffEntity> getStaff() {
        return staff;
    }

    public void setStaff(List<StaffEntity> staff) {
        this.staff = staff;
    }

    public String getFieldImage1() {
        return fieldImage1;
    }

    public void setFieldImage1(String fieldImage1) {
        this.fieldImage1 = fieldImage1;
    }

    public String getFieldImage2() {
        return fieldImage2;
    }

    public void setFieldImage2(String fieldImage2) {
        this.fieldImage2 = fieldImage2;
    }
}
