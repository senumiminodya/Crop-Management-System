package lk.ijse.cropmanagementsystem.repository;

import lk.ijse.cropmanagementsystem.entity.impl.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, String> {
}
