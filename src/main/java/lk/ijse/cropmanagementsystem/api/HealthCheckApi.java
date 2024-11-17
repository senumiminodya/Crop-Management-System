package lk.ijse.cropmanagementsystem.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/health")
@CrossOrigin(origins = "http://localhost:63342", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
/* Meka api hadanne programme eka wada karanawada balanna withrai. */
/* Note controller ekema meka danne natte seperate of concern nathi wena nisa. restfullness walata harm wena nisa */
public class HealthCheckApi {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String healthTest() {
        return "Crop Management System is working.";
    }
}
