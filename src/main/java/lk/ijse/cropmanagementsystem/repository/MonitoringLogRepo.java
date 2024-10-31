package lk.ijse.cropmanagementsystem.repository;

import lk.ijse.cropmanagementsystem.entity.impl.MonitoringLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringLogRepo extends JpaRepository<MonitoringLogEntity, String> {
}
