package lk.ijse.cropmanagementsystem.service;

import lk.ijse.cropmanagementsystem.dto.impl.UserDTO;
import lk.ijse.cropmanagementsystem.secure.JWTAuthResponse;
import lk.ijse.cropmanagementsystem.secure.SignIn;

public interface AuthService {
    JWTAuthResponse signIn(SignIn signIn);
    JWTAuthResponse signUp(UserDTO userDTO);
    JWTAuthResponse refreshToken(String accessToken);
}
