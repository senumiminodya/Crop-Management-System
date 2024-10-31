package lk.ijse.cropmanagementsystem.repository;

import lk.ijse.cropmanagementsystem.entity.impl.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepo extends JpaRepository<FieldEntity, String> {
}
