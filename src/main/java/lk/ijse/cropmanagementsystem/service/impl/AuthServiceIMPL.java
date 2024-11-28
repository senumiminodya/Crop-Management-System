package lk.ijse.cropmanagementsystem.service.impl;

import lk.ijse.cropmanagementsystem.dto.impl.UserDTO;
import lk.ijse.cropmanagementsystem.entity.impl.UserEntity;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.repository.UserRepo;
import lk.ijse.cropmanagementsystem.secure.JWTAuthResponse;
import lk.ijse.cropmanagementsystem.secure.SignIn;
import lk.ijse.cropmanagementsystem.service.AuthService;
import lk.ijse.cropmanagementsystem.service.JWTService;
import lk.ijse.cropmanagementsystem.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceIMPL implements AuthService {
    private final UserRepo userRepo; //RequiredArgsConstructor dala thiyana nisa auto constructor thrugh ingection ekak wenawa.
    private final Mapping mapping;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public JWTAuthResponse signIn(SignIn signIn) {
        //principle user kenek widiyata userwa register karanna (Security config - Authentication manager)
        //Database eke user innawada kiyala search karanna
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword()));
        var user = userRepo.findByEmail(signIn.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var generatedToken = jwtService.generateToken(user);
        return JWTAuthResponse.builder().token(generatedToken).build();
    }

    @Override
    public JWTAuthResponse signUp(UserDTO userDTO) {
        //save user
        //UserEntity savedUser = userRepo.save(mapping.toUserEntity(userDTO));
        //generate a token and return it
        //var generatedToken = jwtService.generateToken(savedUser);
        //return JWTAuthResponse.builder().token(generatedToken).build();
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        try {
            UserEntity savedUser = userRepo.save(mapping.toUserEntity(userDTO));
            var generatedToken = jwtService.generateToken(savedUser);
            return JWTAuthResponse.builder().token(generatedToken).build();
        } catch (DataPersistException e) {
            // Handle DataPersistException or other relevant exceptions
            // You can log the error and return an appropriate error response
            e.printStackTrace();
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }

    @Override
    public JWTAuthResponse refreshToken(String accessToken) {
        //mulinma user kawda kiyala hoyaganna one.(extract username(email))
        var userName = jwtService.extractUserName(accessToken);
        //check the user availability in the DB
        var findUser = userRepo.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var refreshToken = jwtService.refreshToken(findUser);
        return JWTAuthResponse.builder().token(refreshToken).build();
    }
}
