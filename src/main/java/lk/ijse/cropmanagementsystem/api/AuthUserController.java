package lk.ijse.cropmanagementsystem.api;

import lk.ijse.cropmanagementsystem.dto.impl.UserDTO;
import lk.ijse.cropmanagementsystem.entity.Role;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.secure.JWTAuthResponse;
import lk.ijse.cropmanagementsystem.secure.SignIn;
import lk.ijse.cropmanagementsystem.service.AuthService;
import lk.ijse.cropmanagementsystem.service.UserService;
import lk.ijse.cropmanagementsystem.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("api/v1/auth/")
@RestController
@RequiredArgsConstructor
public class AuthUserController {
    private final UserService userService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping(value = "signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTAuthResponse> saveUser(
            @RequestPart("firstName") String firstName,
            @RequestPart("lastName") String lastName,
            @RequestPart("email") String email,
            @RequestPart("password") String password,
            @RequestPart("role") String role
    ) {
        try {
            //UserId generate
            String userId = AppUtil.generateUserId();
            //Build the Object
            UserDTO buildUserDTO = new UserDTO();
            buildUserDTO.setUserId(userId);
            buildUserDTO.setFirstName(firstName);
            buildUserDTO.setLastName(lastName);
            buildUserDTO.setEmail(email);
            buildUserDTO.setPassword(passwordEncoder.encode(password));
            buildUserDTO.setRole(Role.valueOf(role));
            return ResponseEntity.ok(authService.signUp(buildUserDTO));
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
