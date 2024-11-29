package lk.ijse.cropmanagementsystem.api;

import lk.ijse.cropmanagementsystem.dto.StaffStatus;
import lk.ijse.cropmanagementsystem.dto.impl.StaffDTO;
import lk.ijse.cropmanagementsystem.dto.impl.UserDTO;
import lk.ijse.cropmanagementsystem.entity.Role;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.secure.JWTAuthResponse;
import lk.ijse.cropmanagementsystem.secure.SignIn;
import lk.ijse.cropmanagementsystem.service.AuthService;
import lk.ijse.cropmanagementsystem.service.StaffService;
import lk.ijse.cropmanagementsystem.service.UserService;
import lk.ijse.cropmanagementsystem.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("api/v1/auth/")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AuthUserApi {
    private final UserService userService;
    private final AuthService authService;
    private final StaffService staffService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping(value = "signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public ResponseEntity<JWTAuthResponse> saveUser(@RequestBody UserDTO userDTO) {
        System.out.println("comes save user method in AuthUserApi");
        System.out.println(userDTO);
        try {
            // Check if a staff member exists with the given email
            Optional<StaffDTO> existingStaff = staffService.findByEmail(userDTO.getEmail());

            if (!existingStaff.isPresent()) {
                System.out.println("this is not a existing customer!!!");
                // Save new staff member if none exists
                StaffDTO newStaff = new StaffDTO();
                newStaff.setEmail(userDTO.getEmail());
                newStaff.setRole(userDTO.getRole());


                newStaff = staffService.saveStaff(newStaff);

                // Set the saved staff ID to the user DTO
                userDTO.setId(newStaff.getId());
            } else {
                System.out.println("This is an existing customer");
                // Link to the existing staff member
                userDTO.setId(existingStaff.get().getId());
            }

            // Save the user
            return ResponseEntity.ok(authService.signUp(userDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = "signin",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTAuthResponse> signIn(@RequestBody SignIn signIn){
        // Apu userwa application eke save karannna
        // userta token ekak generate karanna
        return ResponseEntity.ok(authService.signIn(signIn));

    }
    @PostMapping("refresh")
    public ResponseEntity<JWTAuthResponse> signIn(@RequestParam("existingToken") String existingToken) {
        return ResponseEntity.ok(authService.refreshToken(existingToken));
    }
}
