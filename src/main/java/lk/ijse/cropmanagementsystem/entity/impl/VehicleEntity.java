package lk.ijse.cropmanagementsystem.entity.impl;

import jakarta.persistence.*;
import lk.ijse.cropmanagementsystem.entity.StatusOfVehicle;
import lk.ijse.cropmanagementsystem.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "vehicle")
public class VehicleEntity implements SuperEntity {
    @Id
    private String vehicleCode;
    private String licensePlateNumber;
    private String category;
    private String fuelType;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusOfVehicle status;

    @ManyToOne
    @JoinColumn(name = "staffId")
    private StaffEntity staff;

    private String remarks;

}
