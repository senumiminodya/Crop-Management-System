package lk.ijse.cropmanagementsystem.api;

import lk.ijse.cropmanagementsystem.customStatusCode.SelectedClassesErrorStatus;
import lk.ijse.cropmanagementsystem.dto.VehicleStatus;
import lk.ijse.cropmanagementsystem.dto.impl.VehicleDTO;
import lk.ijse.cropmanagementsystem.exception.DataPersistException;
import lk.ijse.cropmanagementsystem.exception.VehicleNotFoundException;
import lk.ijse.cropmanagementsystem.service.VehicleService;
import lk.ijse.cropmanagementsystem.util.RegexProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/vehicles")
@CrossOrigin(origins = "http://localhost:63342", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class VehicleApi {
    private static final Logger logger = LoggerFactory.getLogger(VehicleApi.class);
    @Autowired
    private VehicleService vehicleService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveVehicle(@RequestBody VehicleDTO vehicleDTO) {
        try {
            vehicleService.saveVehicle(vehicleDTO);
            logger.info("Vehicle saved successfully: {}", vehicleDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataPersistException e){
            logger.error("Error saving vehicle: {}", vehicleDTO, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            logger.error("Unexpected error saving vehicle: {}", vehicleDTO, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/{vehicleCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleStatus getSelectedVehicle(@PathVariable ("vehicleCode") String vehicleCode){
        if (!RegexProcess.vehicleCodeMatcher(vehicleCode)) {
            logger.warn("Invalid vehicle code: {}", vehicleCode);
            return new SelectedClassesErrorStatus(1,"Vehicle Code is not valid");
        }
        return vehicleService.getVehicle(vehicleCode);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDTO> getALlVehicles(){
        logger.info("Received request to get all vehicles");
        return vehicleService.getAllVehicles();
    }
    @DeleteMapping(value = "/{vehicleCode}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable ("vehicleCode") String vehicleCode){
        try {
            if (!RegexProcess.vehicleCodeMatcher(vehicleCode)) {
                logger.warn("Invalid vehicle code for deletion: {}", vehicleCode);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            vehicleService.deleteVehicle(vehicleCode);
            logger.info("Vehicle with code {} deleted successfully", vehicleCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (VehicleNotFoundException e){
            logger.error("Vehicle not found for deletion: {}", vehicleCode, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error("Unexpected error deleting vehicle with code: {}", vehicleCode, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/{vehicleCode}")
    public ResponseEntity<Void> updateVehicle(@PathVariable ("vehicleCode") String vehicleCode,
                                           @RequestBody VehicleDTO updatedVehicleDTO){
        //validations
        try {
            if(!RegexProcess.vehicleCodeMatcher(vehicleCode) || updatedVehicleDTO == null){
                logger.warn("Invalid vehicle code or empty vehicle data for update: {}", vehicleCode);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            vehicleService.updateVehicle(vehicleCode,updatedVehicleDTO);
            logger.info("Vehicle with code {} updated successfully", vehicleCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (VehicleNotFoundException e){
            logger.error("Vehicle not found for update: {}", vehicleCode, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
