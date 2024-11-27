package lk.ijse.cropmanagementsystem.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.UserStatus;
import lk.ijse.cropmanagementsystem.dto.impl.UserDTO;
import lk.ijse.cropmanagementsystem.entity.impl.UserEntity;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.UserNotFoundException;
import lk.ijse.cropmanagementsystem.repository.UserRepo;
import lk.ijse.cropmanagementsystem.service.UserService;
import lk.ijse.cropmanagementsystem.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceIMPL implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private Mapping mapping;
    @Override
    public void saveUser(UserDTO userDTO) {
        UserEntity savedUser =
                userRepo.save(mapping.toUserEntity(userDTO));
        if (savedUser == null) {
            throw new DataPersistException("User not saved");
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> allUsers = userRepo.findAll();
        return mapping.asUserDTOList(allUsers);
    }

    @Override
    public UserStatus getUser(String userId) {
        if(userRepo.existsById(userId)){
            UserEntity selectedUser = userRepo.getReferenceById(userId);
            return mapping.toUserDTO(selectedUser);
        }else {
            return new SelectedClassesErrorStatus(2, "User with id " + userId + " not found");
        }
    }

    @Override
    public void deleteUser(String userId) {
        Optional<UserEntity> existedUser = userRepo.findById(userId);
        if(!existedUser.isPresent()){
            throw new UserNotFoundException("User with id " + userId + " not found");
        }else {
            userRepo.deleteById(userId);
        }
    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {
        Optional<UserEntity> tmpUser = userRepo.findById(userId);
        if(tmpUser.isPresent()) {
            tmpUser.get().setFirstName(userDTO.getFirstName());
            tmpUser.get().setLastName(userDTO.getLastName());
            tmpUser.get().setEmail(userDTO.getEmail());
            tmpUser.get().setPassword(userDTO.getPassword());
        }
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username ->
                userRepo.findByEmail(username)
                        .orElseThrow(()->new UserNotFoundException());
    }
}
