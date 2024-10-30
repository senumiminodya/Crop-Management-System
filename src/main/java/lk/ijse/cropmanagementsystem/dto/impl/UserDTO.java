package lk.ijse.cropmanagementsystem.dto.impl;

import lk.ijse.cropmanagementsystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String email;
    private String password;
    private Role role;
    private StaffDTO staff;
}
