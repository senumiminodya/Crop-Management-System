package lk.ijse.cropmanagementsystem.entity.impl;

import jakarta.persistence.*;
import lk.ijse.cropmanagementsystem.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "monitoringLog")
public class MonitoringLogEntity implements SuperEntity {
    @Id
    private String logCode;
    private Date logDate;
    private String observation;

    @Column(columnDefinition = "VARCHAR(255)")
    private String observedImage;

    @ManyToMany
    @JoinTable(
            name = "Log_Field",
            joinColumns = @JoinColumn(name = "logCode"),
            inverseJoinColumns = @JoinColumn(name = "fieldCode")
    )
    private List<FieldEntity> fields;

    @ManyToMany
    @JoinTable(
            name = "Log_Crop",
            joinColumns = @JoinColumn(name = "logCode"),
            inverseJoinColumns = @JoinColumn(name = "cropCode")
    )
    private List<CropEntity> crops;

    @ManyToMany
    @JoinTable(
            name = "Log_Staff",
            joinColumns = @JoinColumn(name = "logCode"),
            inverseJoinColumns = @JoinColumn(name = "staffId")
    )
    private List<StaffEntity> staff;

    public String getLogCode() {
        return logCode;
    }

    public void setLogCode(String logCode) {
        this.logCode = logCode;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getObservedImage() {
        return observedImage;
    }

    public void setObservedImage(String observedImage) {
        this.observedImage = observedImage;
    }

    public List<FieldEntity> getFields() {
        return fields;
    }

    public void setFields(List<FieldEntity> fields) {
        this.fields = fields;
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
}
