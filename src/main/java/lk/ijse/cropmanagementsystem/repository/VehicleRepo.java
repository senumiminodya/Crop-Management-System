package lk.ijse.cropmanagementsystem.repository;

import lk.ijse.cropmanagementsystem.entity.impl.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepo extends JpaRepository<VehicleEntity, String> {
}
