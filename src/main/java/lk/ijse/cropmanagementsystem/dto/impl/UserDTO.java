package lk.ijse.cropmanagementsystem.dto.impl;

import lk.ijse.cropmanagementsystem.dto.UserStatus;
import lk.ijse.cropmanagementsystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO implements UserStatus {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}