package lk.ijse.cropmanagementsystem.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/health")
/* Meka api hadanne programme eka wada karanawada balanna withrai. */
/* Note controller ekema meka danne natte seperate of concern nathi wena nisa. restfullness walata harm wena nisa */
public class HealthCheckController {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String healthTest() {
        return "Crop Management System is working.";
    }
}
