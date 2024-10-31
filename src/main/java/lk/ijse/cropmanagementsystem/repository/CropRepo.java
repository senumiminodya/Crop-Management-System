package lk.ijse.cropmanagementsystem.repository;

import lk.ijse.cropmanagementsystem.entity.impl.CropEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropRepo extends JpaRepository<CropEntity, String> {
}
